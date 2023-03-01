package com.larsorbegozo.habbyt.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habbyt.R
import com.example.habbyt.databinding.FragmentMoodBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.model.Mood
import com.larsorbegozo.habbyt.ui.adapter.MoodListAdapter
import com.larsorbegozo.habbyt.ui.viewmodel.MoodViewModel
import com.larsorbegozo.habbyt.ui.viewmodel.MoodViewModelFactory


class MoodFragment : Fragment(), MoodListAdapter.OnItemClickListener {

    private val viewModel: MoodViewModel by activityViewModels {
        MoodViewModelFactory(
            (activity?.application as BaseApplication).moodDatabase.MoodDao()
        )
    }

    //private val navigationArgs: MoodFragmentArgs by navArgs()

    private lateinit var habit: Habit

    private var _binding: FragmentMoodBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoodBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MoodListAdapter(this)

        viewModel.allMoods.observe(this.viewLifecycleOwner) {
            moods -> moods.let {
                adapter.submitList(it)
            }
        }

        val fabButton = activity?.findViewById<FloatingActionButton>(R.id.add_habit_fab)
        fabButton?.setOnClickListener {
            Toast.makeText(activity, "Mood button working", Toast.LENGTH_SHORT).show()
        }

        binding?.apply {
            recyclerViewMood.adapter = adapter
            recyclerViewMood.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onItemClick(mood: Mood) {
        viewModel.onMoodSelected(mood)
    }



}