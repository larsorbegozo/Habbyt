package com.larsorbegozo.habbyt.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.larsorbegozo.habbyt.databinding.ItemHabitBinding
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.model.provider.HabitColorsProvider
import com.larsorbegozo.habbyt.model.provider.HabitIconsProvider

class HabitListAdapter(private val listener: OnItemClickListener, private val context: Context) : ListAdapter<Habit, HabitListAdapter.HabitViewHolder>(DiffCallback){

    inner class HabitViewHolder(private var binding: ItemHabitBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                rootLayout.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val habitPosition = getItem(position)
                        listener.onItemClick(habitPosition)
                    }
                }
                checkbox.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val habitPosition = getItem(position)
                        listener.onCheckboxClick(habitPosition, checkbox.isChecked)
                    }
                }
            }
        }

        fun bind(habit: Habit) {
            binding.habitName.text = habit.name
            binding.checkbox.isChecked = habit.status
            binding.habitIcon.setImageResource(HabitIconsProvider.habitIconLists[habit.image].image)
            binding.cardView.backgroundTintList = ColorStateList.valueOf(getColor(context, HabitColorsProvider.habitIconColorLists[habit.color].color))
        }
    }

    interface OnItemClickListener {
        fun onItemClick(habit: Habit)
        fun onCheckboxClick(habit: Habit, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HabitViewHolder(
            ItemHabitBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Habit>() {
            override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem == newItem
            }
        }
    }
}