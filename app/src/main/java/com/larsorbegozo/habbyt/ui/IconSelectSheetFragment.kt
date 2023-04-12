package com.larsorbegozo.habbyt.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.R
import com.larsorbegozo.habbyt.databinding.FragmentIconSelectSheetBinding
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.model.HabitIcon
import com.larsorbegozo.habbyt.model.IconsProvider
import com.larsorbegozo.habbyt.ui.adapter.IconsAdapter
import com.larsorbegozo.habbyt.viewmodel.HabitViewModel
import com.larsorbegozo.habbyt.viewmodel.HabitViewModelFactory


class IconSelectSheetFragment : DialogFragment(), IconsAdapter.OnItemClickListener {
    private val viewModel: HabitViewModel by activityViewModels() {
        HabitViewModelFactory(
            (activity?.application as BaseApplication).habitDatabase.HabitDao()
        )
    }

    private var _binding: FragmentIconSelectSheetBinding? = null
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null

    private lateinit var customAlertDialogView: View
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIconSelectSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        recyclerView = RecyclerView(requireContext())
        recyclerView!!.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView!!.adapter = IconsAdapter(IconsProvider.habitIconLists, this, this)

        customAlertDialogView = layoutInflater.inflate(R.layout.fragment_icon_select_sheet, null, false)
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())

        materialAlertDialogBuilder //TODO: bottomNavigationBar reappears when open this
            .setTitle("TITULAZO")
            .setView(recyclerView)

        return materialAlertDialogBuilder.create()
    }

    override fun onIconClicked(habitIcon: HabitIcon) {
        viewModel.setIcon(habitIcon)
    }
}