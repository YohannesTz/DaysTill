package com.github.yohannestz.daystill.data.repository

import com.github.yohannestz.daystill.data.local.ReminderDao
import com.github.yohannestz.daystill.data.model.Reminder
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val reminderDao: ReminderDao) {
    fun getAllReminders(): Flow<List<Reminder>> = reminderDao.getAll()
    fun getReminderById(id: Int): Flow<Reminder> = reminderDao.getById(id)

    suspend fun getHomeScreenReminders(): List<Reminder> = reminderDao.getHomeScreenReminders()
    suspend fun updateReminder(reminder: Reminder) = reminderDao.update(reminder)
    suspend fun insertReminder(reminder: Reminder) = reminderDao.insert(reminder)
    suspend fun deleteReminder(id: Int) = reminderDao.delete(id)
}