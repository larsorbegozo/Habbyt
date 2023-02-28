package com.larsorbegozo.habbyt.data.habit

import androidx.room.*
import com.larsorbegozo.habbyt.model.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habit")
    fun getHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE id = :id")
    fun getHabit(id: Long): Flow<Habit>

    // TODO
    @Query("SELECT * FROM habit WHERE name LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)
}