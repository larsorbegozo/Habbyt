package com.larsorbegozo.habbyt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.habbyt.R
import com.example.habbyt.databinding.FragmentDetailHabitBinding
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.ui.viewmodel.HabitViewModel
import com.larsorbegozo.habbyt.ui.viewmodel.HabitViewModelFactory


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
    ): View {
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

        // Bind toolbar
        binding.apply {
            topBar.inflateMenu(R.menu.top_bar_detail_menu)
            topBar.setNavigationIcon(R.drawable.arrow_back)
            topBar.setTitle(R.string.detail_fragment)
            topBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
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