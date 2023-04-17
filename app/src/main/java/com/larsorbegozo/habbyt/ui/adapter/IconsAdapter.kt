package com.larsorbegozo.habbyt.ui.adapter

import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.larsorbegozo.habbyt.R
import com.larsorbegozo.habbyt.databinding.ListIconSelectBinding
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.model.HabitIcon
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

// TODO: Implement this
class IconsAdapter(private val habitIconList: List<HabitIcon>, private val listener: OnItemClickListener, private val dialog: DialogFragment) : RecyclerView.Adapter<IconsAdapter.IconsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IconsViewHolder(ListIconSelectBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: IconsViewHolder, position: Int) {
        val item = habitIconList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return habitIconList.size
    }

    inner class IconsViewHolder(private var binding: ListIconSelectBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                selectIconButton.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val habitIconPosition = habitIconList[position]
                        listener.onIconClicked(habitIconPosition)
                        dialog.dismiss()
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