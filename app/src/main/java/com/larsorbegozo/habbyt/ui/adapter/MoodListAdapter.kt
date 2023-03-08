package com.larsorbegozo.habbyt.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.larsorbegozo.habbyt.databinding.ListItemMoodBinding
import com.larsorbegozo.habbyt.model.Mood

class MoodListAdapter(private val listener: OnItemClickListener) : ListAdapter<Mood, MoodListAdapter.MoodViewHolder>(DiffCallback) {

    inner class MoodViewHolder(private var binding: ListItemMoodBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val moodPosition = getItem(position)
                        listener.onItemClick(moodPosition)
                    }
                }
            }
        }

        fun bind(mood: Mood) {
            binding.apply {
                moodTitle.text = mood.title
                moodText.text = mood.text
                moodDate.text = mood.date
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(mood: Mood)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MoodViewHolder(
            ListItemMoodBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Mood>() {
            override fun areItemsTheSame(oldItem: Mood, newItem: Mood): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Mood, newItem: Mood): Boolean {
                return oldItem == newItem
            }
        }
    }
}