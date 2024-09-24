package com.github.yohannestz.daystill.di

import androidx.room.Room
import com.github.yohannestz.daystill.data.local.AppDatabase
import com.github.yohannestz.daystill.data.repository.ReminderRepository
import com.github.yohannestz.daystill.data.repository.ThemeRepository
import com.github.yohannestz.daystill.ui.add.AddReminderViewModel
import com.github.yohannestz.daystill.ui.edit.EditReminderViewModel
import com.github.yohannestz.daystill.ui.home.HomeViewModel
import com.github.yohannestz.daystill.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "reminder_db"
        ).build()
    }

    single {
        get<AppDatabase>().reminderDao
    }

    single {
        ReminderRepository(get())
    }

    single {
        ThemeRepository(androidContext())
    }

    viewModel {
        MainViewModel(
            get()
        )
    }

    viewModel {
        HomeViewModel(
            get(),
            get()
        )
    }

    viewModel {
        AddReminderViewModel(get())
    }

    viewModel {
        EditReminderViewModel(get())
    }
}