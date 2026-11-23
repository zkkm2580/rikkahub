package me.hg.rikkahub.ui.context

import androidx.compose.runtime.compositionLocalOf
import me.hg.rikkahub.ui.hooks.CustomTtsState

val LocalTTSState = compositionLocalOf<CustomTtsState> { error("Not provided yet") }
