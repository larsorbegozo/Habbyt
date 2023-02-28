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
import com.example.habbyt.databinding.FragmentAddEditHabitBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.ui.viewmodel.HabitViewModel
import com.larsorbegozo.habbyt.ui.viewmodel.HabitViewModelFactory

class AddEditHabitFragment : Fragment() {

    private val viewModel: HabitViewModel by activityViewModels() {
        HabitViewModelFactory(
            (activity?.application as BaseApplication).database.HabitDao()
        )
    }

    private val navigationArgs: AddEditHabitFragmentArgs by navArgs()

    private lateinit var habit: Habit

    private var _binding: FragmentAddEditHabitBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddEditHabitBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // EDIT HABIT
        val id = navigationArgs.id
        if(id > 0) {
            viewModel.getHabit(id).observe(this.viewLifecycleOwner) { selectedHabit ->
                habit = selectedHabit
                bindHabit(habit)
            }
            // DELETE HABIT
            binding?.deleteHabitButton?.visibility = View.VISIBLE
            binding?.deleteHabitButton?.setOnClickListener {
                deleteHabit(habit)
            }
        } else {
            // ADD HABIT
            binding?.saveAction?.setOnClickListener {
                addHabit(
                    binding!!.itemName.text.toString())
            }
            binding?.topBar?.title = "Add Habit"
        }

        // Bind toolbar
        binding?.apply {
            topBar.inflateMenu(R.menu.top_bar_add_menu)
            topBar.setNavigationIcon(R.drawable.arrow_back)
            topBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun addHabit(name: String) {
        viewModel.addHabit(name, false) // false because the new habit doesn't need to be checked
        findNavController().navigate(R.id.action_addEditHabitFragment_to_habitListFragment)
    }

    private fun updateHabit() {
        viewModel.updateHabit(
            navigationArgs.id,
            binding?.itemName?.text.toString(),
            habit.status
        )
        val action = AddEditHabitFragmentDirections.actionAddEditHabitFragmentToHabitListFragment()
        findNavController().navigate(action)
    }

    private fun deleteHabit(habit: Habit) {
        viewModel.deleteHabit(habit)
        findNavController().navigate(
            R.id.action_addEditHabitFragment_to_habitListFragment
        )
    }

    private fun bindHabit(habit: Habit) {
        binding?.topBar?.title = "Edit Habit"
        binding?.itemName?.setText(habit.name)
        binding?.saveAction?.setOnClickListener {
            updateHabit()
        }
    }
}