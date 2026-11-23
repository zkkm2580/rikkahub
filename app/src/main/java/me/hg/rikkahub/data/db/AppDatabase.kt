package me.hg.rikkahub.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import me.rerere.ai.core.TokenUsage
import me.hg.rikkahub.data.db.dao.ConversationDAO
import me.hg.rikkahub.data.db.dao.FavoriteDAO
import me.hg.rikkahub.data.db.dao.FolderDAO
import me.hg.rikkahub.data.db.dao.GenMediaDAO
import me.hg.rikkahub.data.db.dao.ManagedFileDAO
import me.hg.rikkahub.data.db.dao.MediaCreationDAO
import me.hg.rikkahub.data.db.dao.MemoryConsolidationDAO
import me.hg.rikkahub.data.db.dao.MemoryDAO
import me.hg.rikkahub.data.db.dao.MessageNodeDAO
import me.hg.rikkahub.data.db.dao.TranslationHistoryDAO
import me.hg.rikkahub.data.db.dao.UsageRecordDAO
import me.hg.rikkahub.data.db.dao.WorkspaceDAO
import me.hg.rikkahub.data.db.entity.ConversationEntity
import me.hg.rikkahub.data.db.entity.FavoriteEntity
import me.hg.rikkahub.data.db.entity.FolderEntity
import me.hg.rikkahub.data.db.entity.GenMediaEntity
import me.hg.rikkahub.data.db.entity.ManagedFileEntity
import me.hg.rikkahub.data.db.entity.MediaCreationNodeEntity
import me.hg.rikkahub.data.db.entity.MediaCreationRecordEntity
import me.hg.rikkahub.data.db.entity.MediaCreationSessionEntity
import me.hg.rikkahub.data.db.entity.MemoryConsolidationEntity
import me.hg.rikkahub.data.db.entity.MemoryFileEntity
import me.hg.rikkahub.data.db.entity.MessageNodeEntity
import me.hg.rikkahub.data.db.entity.TranslationHistoryEntity
import me.hg.rikkahub.data.db.entity.UsageRecordEntity
import me.hg.rikkahub.data.db.entity.WorkspaceEntity
import me.hg.rikkahub.data.db.migrations.Migration_16_17
import me.hg.rikkahub.data.db.migrations.Migration_22_23
import me.hg.rikkahub.data.db.migrations.Migration_28_29
import me.hg.rikkahub.data.db.migrations.Migration_8_9
import me.hg.rikkahub.utils.JsonInstant

@Database(
    entities = [
        ConversationEntity::class,
        MemoryFileEntity::class,
        MemoryConsolidationEntity::class,
        GenMediaEntity::class,
        MessageNodeEntity::class,
        ManagedFileEntity::class,
        FavoriteEntity::class,
        WorkspaceEntity::class,
        FolderEntity::class,
        MediaCreationSessionEntity::class,
        MediaCreationNodeEntity::class,
        MediaCreationRecordEntity::class,
        TranslationHistoryEntity::class,
        UsageRecordEntity::class,
    ],
    version = 34,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9, spec = Migration_8_9::class),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11),
        AutoMigration(from = 12, to = 13),
        AutoMigration(from = 16, to = 17, spec = Migration_16_17::class),
        AutoMigration(from = 17, to = 18),
        AutoMigration(from = 18, to = 19),
        AutoMigration(from = 19, to = 20),
        AutoMigration(from = 20, to = 21),
        AutoMigration(from = 21, to = 22),
        AutoMigration(from = 22, to = 23, spec = Migration_22_23::class),
        AutoMigration(from = 23, to = 24),
        AutoMigration(from = 24, to = 25),
        AutoMigration(from = 25, to = 26),
        AutoMigration(from = 26, to = 27),
        AutoMigration(from = 28, to = 29, spec = Migration_28_29::class),
        AutoMigration(from = 29, to = 30),
        AutoMigration(from = 31, to = 32),
        AutoMigration(from = 33, to = 34),
    ]
)
@TypeConverters(TokenUsageConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDAO

    abstract fun memoryDao(): MemoryDAO

    abstract fun memoryConsolidationDao(): MemoryConsolidationDAO

    abstract fun genMediaDao(): GenMediaDAO

    abstract fun messageNodeDao(): MessageNodeDAO

    abstract fun managedFileDao(): ManagedFileDAO

    abstract fun favoriteDao(): FavoriteDAO

    abstract fun workspaceDao(): WorkspaceDAO

    abstract fun folderDao(): FolderDAO

    abstract fun mediaCreationDao(): MediaCreationDAO

    abstract fun translationHistoryDao(): TranslationHistoryDAO

    abstract fun usageRecordDao(): UsageRecordDAO
}

object TokenUsageConverter {
    @TypeConverter
    fun fromTokenUsage(usage: TokenUsage?): String {
        return JsonInstant.encodeToString(usage)
    }

    @TypeConverter
    fun toTokenUsage(usage: String): TokenUsage? {
        return JsonInstant.decodeFromString(usage)
    }
}
