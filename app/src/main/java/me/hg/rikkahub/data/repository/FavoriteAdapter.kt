package me.hg.rikkahub.data.repository

import me.hg.rikkahub.data.db.entity.FavoriteEntity
import me.hg.rikkahub.data.model.FavoriteType

interface FavoriteAdapter<T> {
    val type: FavoriteType

    fun buildRefKey(target: T): String

    fun buildFavoriteEntity(
        target: T,
        existing: FavoriteEntity? = null,
        now: Long = System.currentTimeMillis()
    ): FavoriteEntity
}
