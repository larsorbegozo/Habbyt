package com.larsorbegozo.habbyt.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.habbyt.R
import com.example.habbyt.databinding.FragmentAchievementBinding
import com.example.habbyt.databinding.FragmentSettingsBinding
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.ui.viewmodel.HabitViewModel
import com.larsorbegozo.habbyt.ui.viewmodel.HabitViewModelFactory

class SettingsFragment : Fragment() {

    private val viewModel: HabitViewModel by activityViewModels() {
        HabitViewModelFactory(
            (activity?.application as BaseApplication).database.HabitDao()
        )
    }

    //private val navigationArgs: SettingsFragmentArgs by navArgs()

    private lateinit var habit: Habit

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }
}