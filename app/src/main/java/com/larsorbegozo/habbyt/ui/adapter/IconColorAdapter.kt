package com.larsorbegozo.habbyt.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.larsorbegozo.habbyt.databinding.ListColorSelectBinding
import com.larsorbegozo.habbyt.model.HabitIconColor

class IconColorAdapter(private val habitIconColorList: List<HabitIconColor>, private val listener: OnItemClickListener, private val context: Context) : RecyclerView.Adapter<IconColorAdapter.IconsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IconsViewHolder(ListColorSelectBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: IconsViewHolder, position: Int) {
        val item = habitIconColorList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return habitIconColorList.size
    }

    inner class IconsViewHolder(private var binding: ListColorSelectBinding) : RecyclerView.ViewHolder(binding.root) {
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

        fun render(iconColor: HabitIconColor) {
            binding.imageTesting.setColorFilter(getColor(context, iconColor.color))
        }
    }

    interface OnItemClickListener {
        fun onIconClicked(habitIconColor: HabitIconColor)
    }
}