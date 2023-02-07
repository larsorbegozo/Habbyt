package com.example.habbyt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.habbyt.BaseApplication
import com.example.habbyt.R
import com.example.habbyt.databinding.FragmentDetailHabitBinding
import com.example.habbyt.model.Habit
import com.example.habbyt.ui.viewmodel.HabitViewModel
import com.example.habbyt.ui.viewmodel.HabitViewModelFactory


class DetailHabitFragment : Fragment() {

    private val viewModel: HabitViewModel by activityViewModels() {
        HabitViewModelFactory(
            (activity?.application as BaseApplication).database.HabitDao()
        )
    }

    private val navigationArgs: DetailHabitFragmentArgs by navArgs()

    private lateinit var habit: Habit

    private var _binding: FragmentDetailHabitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailHabitBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.id

        viewModel.getHabit(id).observe(this.viewLifecycleOwner) {
            selectedHabit -> habit = selectedHabit
            bindHabit()
        }
    }

    private fun bindHabit() {
        binding.habitName.text = habit.name
        binding.editHabitFab.setOnClickListener {
            val action = DetailHabitFragmentDirections.actionDetailHabitFragmentToAddEditHabitFragment(habit.id)
            findNavController().navigate(action)
        }
    }
}