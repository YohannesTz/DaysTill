package com.github.yohannestz.daystill.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.daystill.data.model.Reminder
import com.github.yohannestz.daystill.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddReminderViewModel(
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddReminderState())
    val state: StateFlow<AddReminderState> = _state.asStateFlow()

    private fun transformState(transformer: AddReminderState.() -> Unit) {
        _state.update {
            val copy = it.copy()
            transformer(copy)
            copy
        }
    }

    fun onEvent(event: AddReminderEvent) {
        when (event) {
            is AddReminderEvent.SetDate -> {
                transformState {
                    date = event.value
                }
            }

            is AddReminderEvent.SetTitle -> {
                transformState {
                    title = event.value
                }
            }

            is AddReminderEvent.SetNote -> {
                transformState {
                    note = event.value
                }
            }

            is AddReminderEvent.AddReminder -> {
                viewModelScope.launch {
                    transformState {
                        isLoading = true
                        error = null
                    }

                    try {
                        reminderRepository.insertReminder(
                            Reminder(
                                title = _state.value.title,
                                note = _state.value.note,
                                date = _state.value.date
                            )
                        )

                        transformState {
                            isLoading = false
                            error = null
                        }

                        event.onReminderAdded.invoke()
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