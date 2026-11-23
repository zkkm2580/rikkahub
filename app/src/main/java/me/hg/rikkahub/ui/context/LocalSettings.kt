package me.hg.rikkahub.ui.context

import androidx.compose.runtime.compositionLocalOf
import me.hg.rikkahub.data.datastore.Settings

val LocalSettings = compositionLocalOf<Settings> {
    error("No SettingsStore provided")
}
