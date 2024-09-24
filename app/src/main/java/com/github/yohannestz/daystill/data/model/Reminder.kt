package com.github.yohannestz.daystill.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val note: String,
    val shouldShowOnHomeScreen: Boolean = false,
    val date: Long
)