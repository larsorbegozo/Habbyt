package com.larsorbegozo.habbyt.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood")
data class Mood(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("text") val text: String,
    @ColumnInfo("date") val date: String
)