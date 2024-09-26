package com.github.yohannestz.daystill.ui.widget

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
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
import java.util.concurrent.TimeUnit

class DaysTillWidget : GlanceAppWidget(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val reminderList = getPinnedReminders()

        provideContent {
            GlanceTheme {
                if (reminderList.isEmpty()) {
                    AppWidgetColumn(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = GlanceModifier
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
                        Text(
                            text = "(°〇°)",
                            modifier = GlanceModifier.padding(bottom = 8.dp),
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                textAlign = TextAlign.Center,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        )
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
                    AppWidgetColumn(
                        modifier = GlanceModifier
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
                        Column(
                            modifier = GlanceModifier.fillMaxHeight(),
                        ) {
                            reminderList.forEach {
                                ReminderItem(context = context, reminder = it)
                                Spacer(GlanceModifier.defaultWeight())
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
}

private fun getDaysRemaining(targetDate: Long): Long {
    val currentDate = System.currentTimeMillis()
    return TimeUnit.MILLISECONDS.toDays(targetDate - currentDate)
}

@Composable
fun ReminderItem(context: Context, reminder: Reminder) {
    Row {
        Image(
            provider = ImageProvider(R.drawable.ic_calendar_event),
            contentDescription = null,
            colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface),
            modifier = GlanceModifier.padding(top = 8.dp)
        )
        Column(
            modifier = GlanceModifier
                .padding(start = 8.dp)
                .fillMaxWidth()
        ) {
            val daysRemaining = getDaysRemaining(reminder.date)

            Text(
                text = reminder.title,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = GlanceTheme.colors.onSurface,
                ),
                modifier = GlanceModifier.fillMaxWidth()
            )

            Text(
                text = reminder.note,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = ColorProvider(
                        GlanceTheme.colors.onSurfaceVariant.getColor(
                            context
                        ).copy(alpha = 0.5f)
                    ),
                ),
                modifier = GlanceModifier.fillMaxWidth()
            )

            if (daysRemaining > 0) {
                Text(
                    text = if (daysRemaining.toInt() == 1) "$daysRemaining day remaining for ${reminder.title}" else "$daysRemaining days remaining for ${reminder.title}",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = ColorProvider(
                            GlanceTheme.colors.onSurfaceVariant.getColor(
                                context
                            ).copy(alpha = 0.5f)
                        ),
                    ),
                    modifier = GlanceModifier.fillMaxWidth()
                )
            }
        }
    }
}