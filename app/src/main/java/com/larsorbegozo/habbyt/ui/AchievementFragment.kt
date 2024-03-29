package com.larsorbegozo.habbyt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.larsorbegozo.habbyt.R
import com.larsorbegozo.habbyt.databinding.FragmentAchievementBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.viewmodel.HabitViewModel
import com.larsorbegozo.habbyt.viewmodel.HabitViewModelFactory


class AchievementFragment : Fragment() {

    private val viewModel: HabitViewModel by activityViewModels() {
        HabitViewModelFactory(
            (activity?.application as BaseApplication).habitDatabase.HabitDao()
        )
    }

    private lateinit var habit: Habit

    private var _binding: FragmentAchievementBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAchievementBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabButton = activity?.findViewById<FloatingActionButton>(R.id.add_habit_fab)
        fabButton?.setOnClickListener {
            Toast.makeText(activity, "Achievement button working!", Toast.LENGTH_SHORT).show()
        }
    }

}