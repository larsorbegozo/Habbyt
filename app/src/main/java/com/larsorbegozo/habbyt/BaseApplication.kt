package com.larsorbegozo.habbyt

import android.app.Application
import com.larsorbegozo.habbyt.data.habit.HabitDatabase
import com.larsorbegozo.habbyt.data.mood.MoodDatabase

class BaseApplication : Application() {

    // provide a HabitDatabase value by lazy here
    val habitDatabase: HabitDatabase by lazy { HabitDatabase.getDatabase(this) }

    val moodDatabase: MoodDatabase by lazy { MoodDatabase.getDatabase(this) }
}