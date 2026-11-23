package me.hg.rikkahub.ui.context

import androidx.compose.runtime.compositionLocalOf
import me.hg.rikkahub.ui.hooks.CustomAsrState

val LocalASRState = compositionLocalOf<CustomAsrState> { error("Not provided yet") }

