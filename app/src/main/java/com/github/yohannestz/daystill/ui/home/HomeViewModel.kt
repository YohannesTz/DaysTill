package com.github.yohannestz.daystill.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.daystill.data.repository.ReminderRepository
import com.github.yohannestz.daystill.data.repository.ThemeRepository
import com.github.yohannestz.daystill.ui.base.ThemeStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ReminderRepository,
    private val themeRepository: ThemeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeState())

    private val _currentTheme = MutableStateFlow(ThemeStyle.FOLLOW_SYSTEM)
    val currentTheme: StateFlow<ThemeStyle> = _currentTheme.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeStyle.FOLLOW_SYSTEM)

    init {
        viewModelScope.launch {
            transformState {
                isLoading = true
                error = null
            }

            try {
                repository.getAllReminders().collect { fetchedReminders ->
                    transformState {
                        reminders = fetchedReminders
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

        viewModelScope.launch {
            themeRepository.currentTheme.collect { theme ->
                _currentTheme.value = theme
            }
        }
    }

    private fun transformState(transformer: HomeState.() -> Unit) {
        _state.update {
            val copy = it.copy()
            transformer(copy)
            copy
        }
    }

    private fun loadReminders() {
        viewModelScope.launch {
            transformState {
                isLoading = true
                error = null
            }

            try {
                delay(500)
                repository.getAllReminders().collect { fetchedReminders ->
                    transformState {
                        reminders = fetchedReminders
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

    private fun updateTheme(theme: ThemeStyle) {
        viewModelScope.launch {
            themeRepository.setTheme(theme)
            _currentTheme.value = theme
        }
    }

    private fun toggleTheme() {
        val newTheme = when (_currentTheme.value) {
            ThemeStyle.DARK -> ThemeStyle.LIGHT
            ThemeStyle.LIGHT -> ThemeStyle.DARK
            ThemeStyle.FOLLOW_SYSTEM -> ThemeStyle.DARK
        }
        updateTheme(newTheme)
    }


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.DeleteReminder -> {
                viewModelScope.launch {
                    transformState {
                        isLoading = true
                        error = null
                    }

                    try {
                        repository.deleteReminder(event.reminder.id)
                    } catch (ex: Exception) {
                        transformState {
                            error = ex.message
                            isLoading = false
                        }
                    }
                }
            }

            is HomeEvent.UpdateReminder -> {
                viewModelScope.launch {
                    transformState {
                        isLoading = true
                        error = null
                    }

                    try {
                        repository.updateReminder(
                            event.reminder
                        )
                    } catch (ex: Exception) {
                        transformState {
                            error = ex.message
                            isLoading = false
                        }
                    }
                }
            }

            is HomeEvent.Refresh -> loadReminders()

            is HomeEvent.ToggleTheme -> toggleTheme()
        }
    }
}
