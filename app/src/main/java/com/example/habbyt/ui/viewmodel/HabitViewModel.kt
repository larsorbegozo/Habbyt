package com.example.habbyt.ui.viewmodel

import androidx.lifecycle.*
import com.example.habbyt.data.HabitDao
import com.example.habbyt.model.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitViewModel(private val habitDao: HabitDao) : ViewModel() {

    var allHabits: LiveData<List<Habit>> = habitDao.getHabits().asLiveData()

    fun getHabit(id: Long): LiveData<Habit> {
        return habitDao.getHabit(id).asLiveData()
    }

    fun addHabit(
        name: String
    ) {
        val habit = Habit(
            name = name
        )

        viewModelScope.launch {
            habitDao.insert(habit)
        }
    }

    fun updateHabit(
        habitId: Long,
        habitName: String
    ) {
        val updatedHabit = Habit(habitId, habitName)
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.update(updatedHabit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.delete(habit)
        }
    }

}

class HabitViewModelFactory(private val habitDao: HabitDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitViewModel(habitDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}