package com.github.yohannestz.daystill.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.yohannestz.daystill.data.model.Reminder

@Database(
    entities = [Reminder::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract val reminderDao: ReminderDao
}