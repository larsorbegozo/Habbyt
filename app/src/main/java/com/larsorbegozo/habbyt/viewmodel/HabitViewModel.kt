package com.larsorbegozo.habbyt.viewmodel

import androidx.lifecycle.*
import com.larsorbegozo.habbyt.R
import com.larsorbegozo.habbyt.data.habit.HabitDao
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.model.HabitIcon
import com.larsorbegozo.habbyt.model.HabitColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel

class HabitViewModel(private val habitDao: HabitDao) : ViewModel() {

    var allHabits: LiveData<List<Habit>> = habitDao.getHabits().asLiveData()

    private val habitEventChannel = Channel<HabitEvent>()
    val habitEvent = habitEventChannel.receiveAsFlow()

    // Used for changing in real time when adding/editing the habit
    private var _tempImageDrawable = MutableLiveData(R.drawable.pedal_bike)
    val tempImageDrawable: LiveData<Int> = _tempImageDrawable

    private var _tempImageColor = MutableLiveData(R.color.icon_red)
    val tempImageColor: LiveData<Int> = _tempImageColor

    // Used for access with ID into the Provider
    private var _tempImageID = MutableLiveData(0)
    val tempImageID: LiveData<Int> = _tempImageID

    private var _tempImageColorID = MutableLiveData(0)
    val tempImageColorID: LiveData<Int> = _tempImageColorID

    fun setIcon(icon: HabitIcon) {
        _tempImageID.value = icon.id
        _tempImageDrawable.value = icon.image
    }

    fun setIconColor(iconColor: HabitColor) {
        _tempImageColorID.value = iconColor.id
        _tempImageColor.value = iconColor.color
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
        status: Boolean,
        color: Int
    ) {
        val habit = Habit(
            name = name,
            description = description,
            image = image,
            goal = goal,
            unit = unit,
            status = status,
            color = color
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
        status: Boolean,
        color: Int
    ) {
        val updatedHabit = Habit(id, name, description, image, goal, unit, status, color)
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.update(updatedHabit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.delete(habit)
        }
    }

    private fun resetTempValues() { //TODO: add this to reset values when necessary
        _tempImageID.value = 0
        _tempImageColorID.value = 0
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