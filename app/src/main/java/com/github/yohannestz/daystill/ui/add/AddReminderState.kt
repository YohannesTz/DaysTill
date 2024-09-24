package com.github.yohannestz.daystill.ui.add

data class AddReminderState(
    var title: String = "",
    var note: String = "",
    var date: Long =  System.currentTimeMillis(),
    var isLoading: Boolean = false,
    var error: String? = null
)