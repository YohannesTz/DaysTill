package com.github.yohannestz.daystill.ui.base.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.github.yohannestz.daystill.ui.base.ThemeStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTopAppBarNavIcon(
    title: String,
    themeStyle: ThemeStyle,
    onThemeToggleIconClick: () -> Unit,
    floatingActionButton: @Composable (() -> Unit) = {},
    contentWindowInsets: WindowInsets = WindowInsets.systemBars,
    content: @Composable (PaddingValues) -> Unit
) {

    Scaffold(
        topBar = {
            ThemeIconTopAppBar(
                title = title,
                themeStyle = themeStyle,
                onThemeToggleIconClick = onThemeToggleIconClick
            )
        },
        floatingActionButton = floatingActionButton,
        contentWindowInsets = contentWindowInsets,
        content = content
    )
}