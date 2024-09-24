package com.github.yohannestz.daystill.ui.home

import com.github.yohannestz.daystill.data.model.Reminder

data class HomeState (
    var reminders: List<Reminder> = emptyList(),
    var isLoading: Boolean = false,
    var error: String? = null
)