package com.github.yohannestz.daystill.ui.edit

data class EditReminderState(
    var id: Int = 0,
    var title: String = "",
    var note: String = "",
    var date: Long = System.currentTimeMillis(),
    var isLoading: Boolean = false,
    var error: String? = null
)