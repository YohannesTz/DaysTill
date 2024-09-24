package com.github.yohannestz.daystill.ui.base

import androidx.compose.runtime.compositionLocalOf

val LocalDaysTillTheme = compositionLocalOf { ThemeStyle.FOLLOW_SYSTEM }

enum class ThemeStyle {
    DARK,
    LIGHT,
    FOLLOW_SYSTEM
}