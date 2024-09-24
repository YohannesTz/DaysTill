package com.github.yohannestz.daystill.ui.base.composables

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.github.yohannestz.daystill.ui.base.ThemeStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeIconTopAppBar(
    title: String,
    themeStyle: ThemeStyle,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onThemeToggleIconClick: () -> Unit
) {

    val animateThemeToggleRotation by animateFloatAsState(
        targetValue = if (themeStyle == ThemeStyle.DARK) 0f else 360f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "theme_toggle_rotation"
    )

    TopAppBar(
        title = {
            Row {
                Text(text = title)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onThemeToggleIconClick
                ) {
                    Icon(
                        painter = rememberVectorPainter(
                            image = if (themeStyle == ThemeStyle.LIGHT) rememberDarkThemeIcon(
                                tint = MaterialTheme.colorScheme.onSurface
                            ) else rememberLightThemeIcon(
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        ),
                        contentDescription = "Toggle Theme",
                        modifier = Modifier.rotate(animateThemeToggleRotation),
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}
