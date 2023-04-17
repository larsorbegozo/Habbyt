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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.R
import com.larsorbegozo.habbyt.databinding.FragmentIconSelectSheetBinding
import com.larsorbegozo.habbyt.model.HabitIcon
import com.larsorbegozo.habbyt.model.HabitIconColor
import com.larsorbegozo.habbyt.model.IconColorsProvider
import com.larsorbegozo.habbyt.model.IconsProvider
import com.larsorbegozo.habbyt.ui.adapter.IconColorAdapter
import com.larsorbegozo.habbyt.ui.adapter.IconsAdapter
import com.larsorbegozo.habbyt.viewmodel.HabitViewModel
import com.larsorbegozo.habbyt.viewmodel.HabitViewModelFactory


class IconSelectSheetFragment : DialogFragment(), IconsAdapter.OnItemClickListener, IconColorAdapter.OnItemClickListener {
    private val viewModel: HabitViewModel by activityViewModels() {
        HabitViewModelFactory(
            (activity?.application as BaseApplication).habitDatabase.HabitDao()
        )
    }

    private var _binding: FragmentIconSelectSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIconSelectSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentIconSelectSheetBinding.inflate(layoutInflater)

        binding.recyclerViewIcons.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.recyclerViewIcons.adapter = IconsAdapter(IconsProvider.habitIconLists, this, this)

        binding.recyclerViewIconsColor.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewIconsColor.adapter = IconColorAdapter(IconColorsProvider.habitIconColorLists, this, requireContext())

        materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())

        materialAlertDialogBuilder //TODO: bottomNavigationBar reappear when open this
            .setTitle("TITULAZO")
            .setView(binding.root)

        return materialAlertDialogBuilder.create()
    }

    override fun onIconClicked(habitIcon: HabitIcon) {
        viewModel.setIcon(habitIcon)
    }

    override fun onIconClicked(habitIconColor: HabitIconColor) {
        viewModel.setIconColor(habitIconColor)
    }
}