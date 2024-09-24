package com.github.yohannestz.daystill.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.daystill.data.repository.ThemeRepository
import com.github.yohannestz.daystill.ui.base.ThemeStyle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val themeRepository: ThemeRepository
) : ViewModel() {

    val theme = themeRepository.currentTheme
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeStyle.FOLLOW_SYSTEM)
}