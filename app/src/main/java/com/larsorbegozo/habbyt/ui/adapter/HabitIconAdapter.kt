package com.larsorbegozo.habbyt.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.larsorbegozo.habbyt.databinding.ItemHabitIconBinding
import com.larsorbegozo.habbyt.model.HabitIcon
import com.larsorbegozo.habbyt.viewmodel.HabitViewModel

class HabitIconAdapter(
    private val habitIconList: List<HabitIcon>,
    private val listener: OnItemClickListener,
    ) : RecyclerView.Adapter<HabitIconAdapter.IconsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IconsViewHolder(ItemHabitIconBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: IconsViewHolder, position: Int) {
        val item = habitIconList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return habitIconList.size
    }

    inner class IconsViewHolder(private var binding: ItemHabitIconBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                selectIconButton.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val habitIconPosition = habitIconList[position]
                        listener.onIconClicked(habitIconPosition)
                    }
                }
            }
        }

        fun render(icon: HabitIcon) {
            binding.selectIconButton.setImageResource(icon.image)
        }
    }

    interface OnItemClickListener {
        fun onIconClicked(habitIcon: HabitIcon)
    }
}