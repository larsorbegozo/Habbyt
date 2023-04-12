package com.larsorbegozo.habbyt.ui.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.larsorbegozo.habbyt.R
import com.larsorbegozo.habbyt.databinding.FragmentDetailHabitBinding
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.model.HabitIcon
import com.larsorbegozo.habbyt.model.IconsProvider
import com.larsorbegozo.habbyt.viewmodel.HabitViewModel
import com.larsorbegozo.habbyt.viewmodel.HabitViewModelFactory


class DetailHabitFragment : Fragment() {

    private val viewModel: HabitViewModel by activityViewModels() {
        HabitViewModelFactory(
            (activity?.application as BaseApplication).habitDatabase.HabitDao()
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
            topBar.title = R.string.detail_habit_topbar_title.toString() // TODO: Fix
            topBar.inflateMenu(R.menu.top_bar_detail_menu)
            topBar.setNavigationIcon(R.drawable.arrow_back)
            topBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun bindHabit() {
        binding.apply {
            habitName.text = habit.name
            habitImage.setImageResource(IconsProvider.habitIconLists[habit.image].image)
            habitDescription.text = habit.description
            habitFrequency.text = "FREQUENCY" // TODO: Hardcoded text
            habitGoal.text = habit.goal.toString()
            habitUnit.text = habit.unit
            editHabitFab.setOnClickListener {
                val action =
                    DetailHabitFragmentDirections.actionDetailHabitFragmentToAddEditHabitFragment(
                        habit.id
                    )
                findNavController().navigate(action)
            }
        }
    }
}