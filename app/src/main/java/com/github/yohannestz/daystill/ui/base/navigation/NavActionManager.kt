package com.github.yohannestz.daystill.ui.base.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class NavActionManager(
    private val navController: NavController
) {
    fun goBack() {
        navController.popBackStack()
    }

    fun navigateTo(route: String) {
        navController.navigate(route)
    }

    companion object {
        @Composable
        fun rememberNavActionManager(
            navController: NavController = rememberNavController()
        ) = remember {
            NavActionManager(
                navController
            )
        }
    }
}