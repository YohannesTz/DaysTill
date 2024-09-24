package com.github.yohannestz.daystill.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.yohannestz.daystill.ui.base.ThemeStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.themeDataStore by preferencesDataStore(name = "theme_preferences")

class ThemeRepository(private val context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_key")
    }

    // Get the current theme as a Flow, defaulting to FOLLOW_SYSTEM if not set
    val currentTheme: Flow<ThemeStyle> = context.themeDataStore.data
        .map { preferences ->
            val themeName = preferences[THEME_KEY] ?: ThemeStyle.FOLLOW_SYSTEM.name
            ThemeStyle.valueOf(themeName)
        }

    // Save the selected theme
    suspend fun setTheme(theme: ThemeStyle) {
        context.themeDataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }
}
