package com.github.yohannestz.daystill.ui

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.yohannestz.daystill.di.appModule
import com.github.yohannestz.daystill.ui.widget.DaysTillWidgetUpdateWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class App : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

        val widgetUpdateRequest = PeriodicWorkRequestBuilder<DaysTillWidgetUpdateWorker>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "DaysTillWidgetUpdateWorker",
                ExistingPeriodicWorkPolicy.KEEP,
                widgetUpdateRequest
            )
    }
}