package com.example.habbyt

import android.app.Application
import com.example.habbyt.data.HabitDatabase

class BaseApplication : Application() {

    // provide a HabitDatabase value by lazy here
    val database: HabitDatabase by lazy { HabitDatabase.getDatabase(this) }
}