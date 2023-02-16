package com.example.habbyt.ui.viewmodel

import androidx.lifecycle.*
import com.example.habbyt.data.HabitDao
import com.example.habbyt.model.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel

class HabitViewModel(private val habitDao: HabitDao) : ViewModel() {

    var allHabits: LiveData<List<Habit>> = habitDao.getHabits().asLiveData()

    private val habitEventChannel = Channel<HabitEvent>()
    val habitEvent = habitEventChannel.receiveAsFlow()

    fun getHabit(id: Long): LiveData<Habit> {
        return habitDao.getHabit(id).asLiveData()
    }

    fun addHabit(
        name: String,
        status: Boolean
    ) {
        val habit = Habit(
            name = name,
            status = status
        )

        viewModelScope.launch {
            habitDao.insert(habit)
        }
    }

    fun updateHabit(
        id: Long,
        name: String,
        status: Boolean
    ) {
        val updatedHabit = Habit(id, name, status)
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.update(updatedHabit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.delete(habit)
        }
    }

    fun onHabitSelected(habit: Habit) {
        viewModelScope.launch {
            habitEventChannel.send(HabitEvent.NavigateToDetailHabitScreen(habit))
        }
    }

    fun onHabitCheckedChanged(habit: Habit, checked: Boolean) {
        viewModelScope.launch {
            habitDao.update(habit.copy(status = checked))
        }
    }

    sealed class HabitEvent {
        data class NavigateToDetailHabitScreen(val habit: Habit) : HabitEvent() // TODO: WTF is this?
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