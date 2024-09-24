package com.github.yohannestz.daystill.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.yohannestz.daystill.data.model.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder")
    fun getAll(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminder WHERE id = :id")
    fun getById(id: Int): Flow<Reminder>

    @Query("SELECT * FROM reminder WHERE id = :id")
    suspend fun getBy(id: Int): Reminder

    @Query("SELECT * FROM reminder WHERE shouldShowOnHomeScreen = 1 LIMIT 2")
    suspend fun getHomeScreenReminders(): List<Reminder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: Reminder)

    @Update
    suspend fun update(reminder: Reminder)

    @Query("DELETE FROM reminder WHERE id = :id")
    suspend fun delete(id: Int)
}