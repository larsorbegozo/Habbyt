package com.example.habbyt.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.habbyt.databinding.ListItemHabitBinding
import com.example.habbyt.model.Habit

class HabitListAdapter(private val onItemClicked: (Habit) -> Unit) : ListAdapter<Habit, HabitListAdapter.HabitViewHolder>(DiffCallback){

    class HabitViewHolder(private var binding: ListItemHabitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(habit: Habit) {
            binding.habit = habit
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HabitViewHolder(
            ListItemHabitBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(habit)
        }
        holder.bind(habit)
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