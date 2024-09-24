package com.github.yohannestz.daystill.ui.home

import com.github.yohannestz.daystill.data.model.Reminder

sealed interface HomeEvent {
    data object Refresh: HomeEvent
    data object ToggleTheme: HomeEvent

    data class UpdateReminder(val reminder: Reminder) : HomeEvent
    data class DeleteReminder(val reminder: Reminder) : HomeEvent
}