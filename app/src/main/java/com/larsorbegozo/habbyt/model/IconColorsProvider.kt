package com.larsorbegozo.habbyt.model

import com.larsorbegozo.habbyt.R

class IconColorsProvider {
    companion object {
        val habitIconColorLists: List<HabitIconColor> = listOf(
            HabitIconColor(0, "orange", R.color.orange_200),
            HabitIconColor(1, "red", R.color.red_700),
            HabitIconColor(2, "red300", R.color.red_300)
        )
    }
}