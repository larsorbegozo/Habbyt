package com.larsorbegozo.habbyt.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.larsorbegozo.habbyt.R
import com.larsorbegozo.habbyt.databinding.FragmentHabitListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.ui.adapter.HabitListAdapter
import com.larsorbegozo.habbyt.ui.viewmodel.HabitViewModel
import com.larsorbegozo.habbyt.ui.viewmodel.HabitViewModelFactory

class HabitListFragment : Fragment(), HabitListAdapter.OnItemClickListener {

    private val viewModel: HabitViewModel by activityViewModels {
        HabitViewModelFactory(
            (activity?.application as BaseApplication).habitDatabase.HabitDao()
        )
    }
    // Backing property is recommended for Fragments, I think.
    private var _binding: FragmentHabitListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHabitListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HabitListAdapter(this)

        viewModel.allHabits.observe(this.viewLifecycleOwner) {
            habits -> habits.let {
                adapter.submitList(it)
            }
        }

        val fabButton = activity?.findViewById<FloatingActionButton>(R.id.add_habit_fab)
        fabButton?.setOnClickListener {
            findNavController().navigate(R.id.action_habitListFragment_to_addEditHabitFragment)
        }

        binding.apply {
            recyclerViewHabit.adapter = adapter
            recyclerViewHabit.layoutManager = LinearLayoutManager(context)
            topBar.setTitle(R.string.list_fragment)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.habitEvent.collect() { event ->
                when(event) {
                    is HabitViewModel.HabitEvent.NavigateToDetailHabitScreen -> {
                        val action = HabitListFragmentDirections.actionHabitListFragmentToDetailHabitFragment(event.habit.id)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onItemClick(habit: Habit) {
        viewModel.onHabitSelected(habit)
    }

    override fun onCheckboxClick(habit: Habit, isChecked: Boolean) {
        viewModel.onHabitCheckedChanged(habit, isChecked)
    }
}