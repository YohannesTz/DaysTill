package com.github.yohannestz.daystill.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.github.yohannestz.daystill.ui.base.navigation.NavActionManager.Companion.rememberNavActionManager
import com.github.yohannestz.daystill.ui.theme.DaysTillTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinAndroidContext {
                val currentTheme by viewModel.theme.collectAsStateWithLifecycle()
                DaysTillTheme(
                    themeStyle = currentTheme,
                    useBlackColors = true
                ) {
                    val backgroundColor = MaterialTheme.colorScheme.surface
                    val navController = rememberNavController()
                    val navActionManager = rememberNavActionManager(navController)

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = backgroundColor
                    ) {
                        MainNavigation(
                            navActionManager = navActionManager,
                            navController = navController
                        )
                    }
                }

            }
        }
    }
}