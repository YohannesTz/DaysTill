package com.github.yohannestz.daystill.ui.widget

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.github.yohannestz.daystill.R
import com.github.yohannestz.daystill.data.model.Reminder
import com.github.yohannestz.daystill.data.repository.ReminderRepository
import com.github.yohannestz.daystill.ui.main.MainActivity
import com.github.yohannestz.daystill.ui.theme.AppWidgetColumn
import com.github.yohannestz.daystill.ui.theme.glanceStringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.time.toDuration

class DaysTillWidget : GlanceAppWidget(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val reminderList = getPinnedReminders()

        provideContent {
            GlanceTheme {
                Log.e("reminderList", "reminderList: ${reminderList.size}")
                val currentTime = Instant.ofEpochMilli(System.currentTimeMillis())
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))


                if (reminderList.isEmpty()) {
                    AppWidgetColumn(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Time is now... $currentTime")
                        Text(
                            text = glanceStringResource(R.string.nothing_today),
                            modifier = GlanceModifier.padding(bottom = 8.dp),
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                } else {
                    AppWidgetColumn {
                        LazyColumn {
                            items(reminderList) { reminder ->
                                Row(
                                    modifier = GlanceModifier
                                        .padding(vertical = 16.dp)
                                ) {
                                    Column(
                                        modifier = GlanceModifier
                                            .padding(bottom = 8.dp)
                                            .fillMaxWidth()
                                            .clickable(
                                                actionStartActivity(
                                                    Intent(
                                                        LocalContext.current,
                                                        MainActivity::class.java
                                                    ).apply {
                                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                    }
                                                )
                                            )
                                    ) {
                                        val daysRemaining = getDaysRemaining(reminder.date)

                                        Text(
                                            text = reminder.title,
                                            style = TextStyle(
                                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                                color = ColorProvider(Color.White),
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = GlanceModifier.fillMaxWidth()
                                        )

                                        Text(
                                            text = reminder.note,
                                            style = TextStyle(
                                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                                color = ColorProvider(Color.White.copy(alpha = 0.7f))
                                            ),
                                            modifier = GlanceModifier.fillMaxWidth()
                                        )

                                        if (daysRemaining > 0) {
                                            Text(
                                                text = if (daysRemaining.toInt() == 1) "$daysRemaining day remaining for ${reminder.title}" else "$daysRemaining days remaining for ${reminder.title}",
                                                style = TextStyle(
                                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                                    color = ColorProvider(Color.White.copy(alpha = 0.7f))
                                                ),
                                                modifier = GlanceModifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun getPinnedReminders(): List<Reminder> {
        return reminderRepository.getHomeScreenReminders()
    }

    private fun getDaysRemaining(targetDate: Long): Long {
        val currentDate = System.currentTimeMillis()
        return TimeUnit.MILLISECONDS.toDays(targetDate - currentDate)
    }
}