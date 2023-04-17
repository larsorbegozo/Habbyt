package com.larsorbegozo.habbyt.model

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("image") val image: Int,
    @ColumnInfo("goal") val goal: Int,
    @ColumnInfo("unit") val unit: String,
    @ColumnInfo("status") val status: Boolean,
    @ColumnInfo("color") val color: Int
)