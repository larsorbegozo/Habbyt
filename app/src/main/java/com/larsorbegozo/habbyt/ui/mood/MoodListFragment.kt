package com.larsorbegozo.habbyt.ui.mood

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.databinding.FragmentMoodListBinding
import com.larsorbegozo.habbyt.model.Mood
import com.larsorbegozo.habbyt.ui.adapter.MoodListAdapter
import com.larsorbegozo.habbyt.viewmodel.MoodViewModel
import com.larsorbegozo.habbyt.viewmodel.MoodViewModelFactory


class MoodListFragment : Fragment(), MoodListAdapter.OnItemClickListener {

    private val viewModel: MoodViewModel by activityViewModels {
        MoodViewModelFactory(
            (activity?.application as BaseApplication).moodDatabase.MoodDao()
        )
    }

    private var _binding: FragmentMoodListBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoodListBinding.inflate(layoutInflater, container, false)
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
            findNavController().navigate(R.id.action_moodFragment_to_addEditMoodFragment)
        }


        binding?.apply {
            recyclerViewMood.adapter = adapter
            recyclerViewMood.layoutManager = LinearLayoutManager(context)
            viewModel.getTotalNotes().observe(viewLifecycleOwner) {
                itemCount.text = it.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.moodEvent.collect() { event ->
                when(event) {
                    is MoodViewModel.MoodEvent.NavigateToDetailMoodScreen -> {
                        val action =
                            MoodListFragmentDirections.actionMoodFragmentToDetailMoodFragment(event.mood.id)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onItemClick(mood: Mood) {
        viewModel.onMoodSelected(mood)
    }
}