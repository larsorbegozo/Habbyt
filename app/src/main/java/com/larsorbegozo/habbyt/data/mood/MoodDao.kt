package com.larsorbegozo.habbyt.data.mood

import androidx.room.*
import com.larsorbegozo.habbyt.model.Mood
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood")
    fun getMoods(): Flow<List<Mood>>

    @Query("SELECT * FROM mood WHERE id = :id")
    fun getMood(id: Long): Flow<Mood>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mood: Mood)

    @Update
    suspend fun update(mood: Mood)

    @Delete
    suspend fun delete(mood: Mood)
}