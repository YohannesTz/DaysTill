package com.github.yohannestz.daystill.ui.edit

sealed interface EditReminderEvent {
    data object SaveReminder : EditReminderEvent

    data class SetTitle(val title: String) : EditReminderEvent
    data class SetNote(val note: String) : EditReminderEvent
    data class SetDate(val date: Long) : EditReminderEvent
}