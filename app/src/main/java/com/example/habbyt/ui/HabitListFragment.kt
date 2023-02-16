package com.example.habbyt.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habbyt.BaseApplication
import com.example.habbyt.R
import com.example.habbyt.databinding.FragmentHabitListBinding
import com.example.habbyt.model.Habit
import com.example.habbyt.ui.adapter.HabitListAdapter
import com.example.habbyt.ui.viewmodel.HabitViewModel
import com.example.habbyt.ui.viewmodel.HabitViewModelFactory

class HabitListFragment : Fragment(), HabitListAdapter.OnItemClickListener {

    private val viewModel: HabitViewModel by activityViewModels {
        HabitViewModelFactory(
            (activity?.application as BaseApplication).database.HabitDao()
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

        binding.apply {
            recyclerViewMenu.adapter = adapter
            recyclerViewMenu.layoutManager = LinearLayoutManager(context)
            addHabitFab.setOnClickListener {
                findNavController().navigate(R.id.action_habitListFragment_to_addEditHabitFragment)
            }
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