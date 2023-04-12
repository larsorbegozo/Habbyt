package com.larsorbegozo.habbyt.viewmodel

import androidx.lifecycle.*
import com.larsorbegozo.habbyt.R
import com.larsorbegozo.habbyt.data.habit.HabitDao
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.model.HabitIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel

class HabitViewModel(private val habitDao: HabitDao) : ViewModel() {

    var allHabits: LiveData<List<Habit>> = habitDao.getHabits().asLiveData()

    private val habitEventChannel = Channel<HabitEvent>()
    val habitEvent = habitEventChannel.receiveAsFlow()

    // ! I think I just need ID variable
    private var _tempImage = MutableLiveData(R.drawable.pedal_bike)
    val tempImage: LiveData<Int> = _tempImage

    private var _tempImageName = MutableLiveData<String>("pedal_bike")
    val tempImageName: LiveData<String> = _tempImageName

    private var _tempImageID = MutableLiveData<Int>(0)
    val tempImageID: LiveData<Int> = _tempImageID

    fun setIcon(icon: HabitIcon) {
        _tempImage.value = icon.image
        _tempImageName.value = icon.name
        _tempImageID.value = icon.id
    }

    fun getHabit(id: Long): LiveData<Habit> {
        return habitDao.getHabit(id).asLiveData()
    }

    fun addHabit(
        name: String,
        description: String,
        image: Int,
        goal: Int,
        unit: String,
        status: Boolean
    ) {
        val habit = Habit(
            name = name,
            description = description,
            image = image,
            goal = goal,
            unit = unit,
            status = status
        )

        viewModelScope.launch {
            habitDao.insert(habit)
        }
    }

    fun updateHabit(
        id: Long,
        name: String,
        description: String,
        image: Int,
        goal: Int,
        unit: String,
        status: Boolean
    ) {
        val updatedHabit = Habit(id, name, description, image, goal, unit, status)
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