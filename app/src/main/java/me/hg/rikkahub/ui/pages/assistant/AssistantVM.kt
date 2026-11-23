package me.hg.rikkahub.ui.pages.assistant

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.hg.rikkahub.AppScope
import me.hg.rikkahub.data.datastore.Settings
import me.hg.rikkahub.data.datastore.SettingsStore
import me.hg.rikkahub.data.files.FilesManager
import me.hg.rikkahub.data.model.Assistant
import me.hg.rikkahub.data.model.Avatar
import me.hg.rikkahub.data.repository.MemoryRepository
import me.hg.rikkahub.data.repository.memoryId
import me.hg.rikkahub.service.ChatService

class AssistantVM(
    private val settingsStore: SettingsStore,
    private val memoryRepository: MemoryRepository,
    private val chatService: ChatService,
    private val filesManager: FilesManager,
    private val appScope: AppScope,
) : ViewModel() {
    val settings: StateFlow<Settings> = settingsStore.settingsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, Settings.dummy())

    fun updateSettings(fn: (Settings) -> Settings) {
        viewModelScope.launch {
            settingsStore.update(fn)
        }
    }

    fun addAssistant(assistant: Assistant) {
        viewModelScope.launch {
            settingsStore.update { it.copy(assistants = it.assistants.plus(assistant)) }
        }
    }

    fun removeAssistant(assistant: Assistant) {
        // 助手从列表移除后用户无法再次触发清理，放到应用级作用域里，离开页面也要跑完
        appScope.launch {
            // 在同一次更新里按最新列表检查，连续删除时不会把最后一个助手也删掉
            var removed: Assistant? = null
            settingsStore.update { settings ->
                val target = settings.assistants.find { it.id == assistant.id }
                if (target == null || settings.assistants.size <= 1) return@update settings
                removed = target
                settings.copy(assistants = settings.assistants.filter { it.id != target.id })
            }
            // 只有真的删掉了才清理关联数据
            val target = removed ?: return@launch
            cleanupAssistantFiles(target)
            memoryRepository.deleteFilesOfAssistant(target.id.toString())
            chatService.deleteConversationsOfAssistant(target.id)
        }
    }

    private fun cleanupAssistantFiles(assistant: Assistant) {
        val uris = buildList {
            (assistant.avatar as? Avatar.Image)?.let { add(it.url.toUri()) }
            assistant.background?.let { add(it.toUri()) }
        }

        if (uris.isNotEmpty()) {
            filesManager.deleteChatFiles(uris)
        }
    }

    fun copyAssistant(assistant: Assistant, copyMemories: Boolean = false) {
        viewModelScope.launch {
            val copiedAssistant = assistant.copy(
                id = kotlin.uuid.Uuid.random(),
                name = "${assistant.name} (Clone)",
                avatar = if (assistant.avatar is Avatar.Image) Avatar.Dummy else assistant.avatar,
            )
            settingsStore.update { it.copy(assistants = it.assistants.plus(copiedAssistant)) }
            if (copyMemories) {
                memoryRepository.copyFiles(
                    fromAssistantId = assistant.id.toString(),
                    toAssistantId = copiedAssistant.id.toString(),
                )
            }
        }
    }

    fun getMemories(assistant: Assistant) = memoryRepository.getFilesFlow(assistant.memoryId)
}
