package com.github.yohannestz.daystill.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.daystill.data.model.Reminder
import com.github.yohannestz.daystill.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditReminderViewModel(
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditReminderState())
    val state: StateFlow<EditReminderState> = _state.asStateFlow()

    private fun transformState(transformer: EditReminderState.() -> Unit) {
        _state.update {
            val copy = it.copy()
            transformer(copy)
            copy
        }
    }

    fun loadReminder(reminderId: Int) {
        viewModelScope.launch {
            transformState {
                id = reminderId
                isLoading = true
                error = null
            }

            try {
                reminderRepository.getReminderById(reminderId).collect { fetchedReminder ->
                    transformState {
                        title = fetchedReminder.title
                        note = fetchedReminder.note
                        date = fetchedReminder.date
                        isLoading = false
                    }
                }
            } catch (ex: Exception) {
                transformState {
                    error = ex.message
                    isLoading = false
                }
            }
        }
    }

    fun onEvent(event: EditReminderEvent) {
        when (event) {
            is EditReminderEvent.SetTitle -> {
                transformState {
                    title = event.title
                }
            }

            is EditReminderEvent.SetNote -> {
                transformState {
                    note = event.note
                }
            }

            is EditReminderEvent.SetDate -> {
                transformState {
                    date = event.date
                }
            }

            is EditReminderEvent.SaveReminder -> {
                viewModelScope.launch {
                    transformState {
                        isLoading = true
                        error = null
                    }

                    try {
                        reminderRepository.updateReminder(
                            Reminder(
                                id = _state.value.id,
                                title = _state.value.title,
                                note = _state.value.note,
                                date = _state.value.date
                            )
                        )

                        transformState {
                            isLoading = false
                            error = null
                        }
                    } catch (ex: Exception) {
                        transformState {
                            error = ex.message
                            isLoading = false
                        }
                    }
                }
            }
        }
    }
}