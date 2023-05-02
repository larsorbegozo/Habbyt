package com.larsorbegozo.habbyt.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.larsorbegozo.habbyt.databinding.ItemHabitColorBinding
import com.larsorbegozo.habbyt.model.HabitColor
import com.larsorbegozo.habbyt.viewmodel.HabitViewModel

class HabitColorAdapter(private val habitIconColorList: List<HabitColor>, private val listener: OnItemClickListener, private val context: Context) : RecyclerView.Adapter<HabitColorAdapter.IconsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IconsViewHolder(ItemHabitColorBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: IconsViewHolder, position: Int) {
        val item = habitIconColorList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return habitIconColorList.size
    }

    inner class IconsViewHolder(private var binding: ItemHabitColorBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                imageTesting.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val habitIconPosition = habitIconColorList[position]
                        listener.onIconClicked(habitIconPosition)
                    }
                }
            }
        }

        fun render(iconColor: HabitColor) {
            binding.imageTesting.setColorFilter(getColor(context, iconColor.color))
        }
    }

    interface OnItemClickListener {
        fun onIconClicked(habitIconColor: HabitColor)
    }
}