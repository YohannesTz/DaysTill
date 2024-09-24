package com.github.yohannestz.daystill.ui.add

sealed interface AddReminderEvent {
    //data object AddReminder: AddReminderEvent
    data class AddReminder(val onReminderAdded: () -> Unit): AddReminderEvent

    data class SetTitle(val value: String): AddReminderEvent
    data class SetNote(val value: String): AddReminderEvent
    data class SetDate(val value: Long): AddReminderEvent
}