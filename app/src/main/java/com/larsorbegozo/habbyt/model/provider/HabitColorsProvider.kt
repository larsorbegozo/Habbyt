package com.larsorbegozo.habbyt.model.provider

import com.larsorbegozo.habbyt.R
import com.larsorbegozo.habbyt.model.HabitColor

class HabitColorsProvider {
    companion object {
        val habitIconColorLists: List<HabitColor> = listOf(
            HabitColor(0, "red", R.color.icon_red),
            HabitColor(1, "pink", R.color.icon_pink),
            HabitColor(2, "purple", R.color.icon_purple),
            HabitColor(3, "deep_purple", R.color.icon_deep_purple),
            HabitColor(4, "indigo", R.color.icon_indigo),
            HabitColor(5, "blue", R.color.icon_blue),
            HabitColor(6, "light_blue", R.color.icon_light_blue),
            HabitColor(7, "cyan", R.color.icon_cyan),
            HabitColor(8, "teal", R.color.icon_teal),
            HabitColor(9, "green", R.color.icon_green),
            HabitColor(10, "light_green", R.color.icon_light_green),
            HabitColor(11, "lime", R.color.icon_lime),
            HabitColor(12, "yellow", R.color.icon_yellow),
            HabitColor(13, "amber", R.color.icon_amber),
            HabitColor(14, "orange", R.color.icon_orange),
            HabitColor(15, "deep_orange", R.color.icon_deep_orange),
            HabitColor(16, "brown", R.color.icon_brown),
            HabitColor(17, "gray", R.color.icon_gray)
        )
    }
}