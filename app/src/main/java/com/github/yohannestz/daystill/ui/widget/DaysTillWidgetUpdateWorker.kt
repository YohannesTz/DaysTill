package com.github.yohannestz.daystill.ui.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DaysTillWidgetUpdateWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(applicationContext)
        val widget = DaysTillWidget()

        val glanceIds = manager.getGlanceIds(DaysTillWidget::class.java)
        glanceIds.forEach { glanceId ->
            widget.update(applicationContext, glanceId)
        }

        return Result.success()
    }
}
