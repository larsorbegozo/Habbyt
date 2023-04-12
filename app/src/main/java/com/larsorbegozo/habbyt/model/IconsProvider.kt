package com.larsorbegozo.habbyt.model

import com.larsorbegozo.habbyt.R

class IconsProvider {
    companion object {
        val habitIconLists: List<HabitIcon> = listOf(
            HabitIcon(0, "pedal_bike", R.drawable.pedal_bike),
            HabitIcon(1, "arrow_back", R.drawable.arrow_back),
            HabitIcon(2, "mood_icon", R.drawable.mood_icon)
        )
    }
}