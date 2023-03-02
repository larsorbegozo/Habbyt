package com.larsorbegozo.habbyt.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.larsorbegozo.habbyt.databinding.FragmentDetailMoodBinding
import androidx.navigation.fragment.navArgs
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.model.Mood
import com.larsorbegozo.habbyt.ui.viewmodel.MoodViewModel
import com.larsorbegozo.habbyt.ui.viewmodel.MoodViewModelFactory


class DetailMoodFragment : Fragment() {

    private val viewModel: MoodViewModel by activityViewModels {
        MoodViewModelFactory(
            (activity?.application as BaseApplication).moodDatabase.MoodDao()
        )
    }

    private val navigationArgs: DetailMoodFragmentArgs by navArgs()

    private lateinit var mood: Mood

    private var _binding: FragmentDetailMoodBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailMoodBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.id

        viewModel.getMood(id).observe(this.viewLifecycleOwner) {
            selectedMood -> mood = selectedMood
            bindMood()
        }
    }

    private fun bindMood() {
        binding?.apply {
            noteTitle.text = mood.title
            noteText.text = mood.text
            fabAddMood.setOnClickListener {
                val action = DetailMoodFragmentDirections.actionDetailMoodFragmentToAddEditMoodFragment(mood.id)
                findNavController().navigate(action)
            }
        }
    }
}