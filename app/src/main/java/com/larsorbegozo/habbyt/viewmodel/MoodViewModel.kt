package com.larsorbegozo.habbyt.viewmodel

import androidx.lifecycle.*
import com.larsorbegozo.habbyt.data.mood.MoodDao
import com.larsorbegozo.habbyt.model.Mood
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MoodViewModel (private val moodDao: MoodDao) : ViewModel() {

    var allMoods: LiveData<List<Mood>> = moodDao.getMoods().asLiveData()

    private val moodEventChannel = Channel<MoodEvent>()
    val moodEvent = moodEventChannel.receiveAsFlow()

    fun getMood(id: Long): LiveData<Mood> {
        return moodDao.getMood(id).asLiveData()
    }

    fun getTotalNotes(): LiveData<Int> {
        return moodDao.getTotalNotes().asLiveData()
    }

    fun addMood(
        title: String,
        text: String,
        date: String,
        hour: String
    ) {
        val mood = Mood(
            title = title,
            text = text,
            date = date,
            hour = hour
        )

        viewModelScope.launch {
            moodDao.insert(mood)
        }
    }

    fun updateMood(
        id: Long,
        title: String,
        text: String,
        date: String,
        hour: String
    ) {
        val updatedMood = Mood(id, title, text, date, hour)
        viewModelScope.launch(Dispatchers.IO) {
            moodDao.update(updatedMood)
        }
    }

    fun deleteMood(mood: Mood) {
        viewModelScope.launch(Dispatchers.IO) {
            moodDao.delete(mood)
        }
    }

    fun onMoodSelected(mood: Mood) {
        viewModelScope.launch {
            moodEventChannel.send(MoodEvent.NavigateToDetailMoodScreen(mood))
        }
    }

    sealed class MoodEvent {
        data class NavigateToDetailMoodScreen(val mood: Mood) : MoodEvent()
    }
}

class MoodViewModelFactory(private val moodDao: MoodDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoodViewModel(moodDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}