package com.github.yohannestz.daystill.ui.base.navigation

enum class Surface(val value: String, val base: Route) {
    HomeView("${Route.Home.value}/home", base = Route.Home),
    AddReminderView("${Route.Home.value}/add", base = Route.Home),
    EditReminderView("${Route.Home.value}/edit", base = Route.Home)
}