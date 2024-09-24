package com.github.yohannestz.daystill.ui.main

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.yohannestz.daystill.ui.add.AddReminderView
import com.github.yohannestz.daystill.ui.base.navigation.NavActionManager
import com.github.yohannestz.daystill.ui.base.navigation.Route
import com.github.yohannestz.daystill.ui.base.navigation.Surface
import com.github.yohannestz.daystill.ui.edit.EditReminderView
import com.github.yohannestz.daystill.ui.home.HomeView

@Composable
fun MainNavigation(
    navController: NavHostController,
    navActionManager: NavActionManager,
    modifier: Modifier = Modifier,
    startDestination: String = Route.Home.value
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            fadeIn() + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )
        },
        exitTransition = {
            fadeOut() + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )
        },
        popEnterTransition = {
            fadeIn()
        },
        popExitTransition = {
            fadeOut() + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )
        }
    ) {
        navigation(route = Route.Home.value, startDestination = Surface.HomeView.value) {
            composable(
                Surface.HomeView.value,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() },
                popExitTransition = { fadeOut() },
            ) {
                HomeView(
                    navActionManager = navActionManager
                )
            }

            composable(
                Surface.AddReminderView.value,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() },
                popExitTransition = { fadeOut() },
            ) {
                AddReminderView(
                    navActionManager = navActionManager
                )
            }

            composable(
                Surface.EditReminderView.value + "/{id}",
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() },
                popExitTransition = { fadeOut() },
            ) { backStackEntry ->
                val reminderIdString = backStackEntry.arguments?.getString("id")!!
                val reminderId = reminderIdString.toInt()

                EditReminderView(
                    reminderId = reminderId,
                    navActionManager = navActionManager
                )
            }
        }
    }
}