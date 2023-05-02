package com.larsorbegozo.habbyt.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.databinding.FragmentHabitIconDialogBinding
import com.larsorbegozo.habbyt.model.HabitColor
import com.larsorbegozo.habbyt.model.HabitIcon
import com.larsorbegozo.habbyt.model.provider.HabitColorsProvider
import com.larsorbegozo.habbyt.model.provider.HabitIconsProvider
import com.larsorbegozo.habbyt.ui.adapter.HabitColorAdapter
import com.larsorbegozo.habbyt.ui.adapter.HabitIconAdapter
import com.larsorbegozo.habbyt.viewmodel.HabitViewModel
import com.larsorbegozo.habbyt.viewmodel.HabitViewModelFactory


class HabitIconDialogFragment : DialogFragment(), HabitIconAdapter.OnItemClickListener, HabitColorAdapter.OnItemClickListener {
    private val viewModel: HabitViewModel by activityViewModels() {
        HabitViewModelFactory(
            (activity?.application as BaseApplication).habitDatabase.HabitDao()
        )
    }

    private var _binding: FragmentHabitIconDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitIconDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentHabitIconDialogBinding.inflate(layoutInflater)

        binding.recyclerViewIcons.layoutManager = GridLayoutManager(requireContext(), 5)
        binding.recyclerViewIcons.adapter = HabitIconAdapter(HabitIconsProvider.habitIconLists, this)

        binding.recyclerViewIconsColor.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewIconsColor.adapter = HabitColorAdapter(HabitColorsProvider.habitIconColorLists, this, requireContext())

        materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())

        materialAlertDialogBuilder
            .setView(binding.root) // TODO: improve positive button to save when pressed, for better user experience
            .setPositiveButton("OK") {_, _ ->
                dialog?.dismiss()
            }

        return materialAlertDialogBuilder.create()
    }

    override fun onIconClicked(habitIcon: HabitIcon) {
        viewModel.setIcon(habitIcon)
    }

    override fun onIconClicked(habitIconColor: HabitColor) {
        viewModel.setIconColor(habitIconColor)
    }
}