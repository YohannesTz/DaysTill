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
import java.util.Calendar
import java.util.concurrent.TimeUnit

class App : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

        val delay = calculateInitialDelay()

        val widgetUpdateRequest =
            PeriodicWorkRequestBuilder<DaysTillWidgetUpdateWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "DaysTillWidgetUpdateWorker",
                ExistingPeriodicWorkPolicy.KEEP,
                widgetUpdateRequest
            )
    }

    private fun calculateInitialDelay(): Long {
        val currentTime = Calendar.getInstance()

        val targetTime = Calendar.getInstance().apply {
            if (get(Calendar.HOUR_OF_DAY) >= 6) {
                add(Calendar.DAY_OF_MONTH, 1)
            }

            set(Calendar.HOUR_OF_DAY, 6)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return targetTime.timeInMillis - currentTime.timeInMillis
    }
}