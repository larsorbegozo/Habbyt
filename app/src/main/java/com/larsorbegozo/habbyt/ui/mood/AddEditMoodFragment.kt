package com.larsorbegozo.habbyt.ui.mood

import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.larsorbegozo.habbyt.R
import com.larsorbegozo.habbyt.databinding.FragmentAddEditMoodBinding
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.model.Mood
import com.larsorbegozo.habbyt.viewmodel.HabitViewModelFactory
import com.larsorbegozo.habbyt.viewmodel.MoodViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AddEditMoodFragment : Fragment() {

    private val viewModel: MoodViewModel by activityViewModels() {
        HabitViewModelFactory(
            (activity?.application as BaseApplication).habitDatabase.HabitDao()
        )
    }

    private val navigationArgs: AddEditMoodFragmentArgs by navArgs()

    private lateinit var mood: Mood

    private var _binding: FragmentAddEditMoodBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddEditMoodBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.id
        if(id > 0) {
            // EDIT MOOD
            viewModel.getMood(id).observe(this.viewLifecycleOwner) { selectedMood ->
                mood = selectedMood
                bindMood(mood)
            }
            // DELETE MOOD
            binding?.deleteHabitButton?.visibility = View.VISIBLE
            binding?.deleteHabitButton?.setOnClickListener {
                deleteMood(mood)
            }
        } else {
            binding?.saveAction?.setOnClickListener {
                addMood(
                    binding!!.noteTitleInput.text.toString(),
                    binding!!.noteTextInput.text.toString()
                )
            }
        }
    }

    private fun bindMood(mood: Mood) {
        binding?.apply {
            noteTitleInput.setText(mood.title)
            noteTextInput.setText(mood.text)
            saveAction.setOnClickListener {
                updateMood()
            }
        }
    }

    private fun addMood(title: String, text: String) {
        // ! This only works if I use the API 26
        val currentDate = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        val dateFormatted = currentDate.format(dateFormatter).toString()

        val currentHour = LocalDateTime.now()
        val hourFormatter = DateTimeFormatter.ofPattern("kk:mm") // TODO: Fit depending on the locale
        val hourFormatted = currentHour.format(hourFormatter).toString()

        viewModel.addMood(title, text, dateFormatted, hourFormatted)
        findNavController().navigate(R.id.action_addEditMoodFragment_to_moodFragment)
    }

    private fun updateMood() {
        viewModel.updateMood(
            navigationArgs.id,
            binding?.noteTitleInput?.text.toString(),
            binding?.noteTextInput?.text.toString(),
            mood.date,
            mood.hour
        )
        val action = AddEditMoodFragmentDirections.actionAddEditMoodFragmentToMoodFragment()
        findNavController().navigate(action)
    }

    private fun deleteMood(mood: Mood) {
        viewModel.deleteMood(mood)
        findNavController().navigate(
            R.id.action_addEditMoodFragment_to_moodFragment
        )
    }
}