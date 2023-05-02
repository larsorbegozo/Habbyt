package com.larsorbegozo.habbyt.ui.habits

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.larsorbegozo.habbyt.R
import com.larsorbegozo.habbyt.BaseApplication
import com.larsorbegozo.habbyt.MainActivity
import com.larsorbegozo.habbyt.databinding.FragmentAddEditHabitBinding
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.model.provider.HabitColorsProvider
import com.larsorbegozo.habbyt.model.provider.HabitIconsProvider
import com.larsorbegozo.habbyt.notification.AlarmNotification
import com.larsorbegozo.habbyt.ui.HabitIconDialogFragment
import com.larsorbegozo.habbyt.viewmodel.HabitViewModel
import com.larsorbegozo.habbyt.viewmodel.HabitViewModelFactory
import java.util.Calendar

class AddEditHabitFragment : Fragment() {
    private val viewModel: HabitViewModel by activityViewModels {
        HabitViewModelFactory(
            (activity?.application as BaseApplication).habitDatabase.HabitDao()
        )
    }

    private val navigationArgs: AddEditHabitFragmentArgs by navArgs()

    private lateinit var habit: Habit
    private lateinit var materialAlertDialog: MaterialAlertDialogBuilder

    private var _binding: FragmentAddEditHabitBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createChannel()
    }

    private fun createChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MainActivity.MY_CHANNEL_ID,
                "MyTestingChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Testeame esta"
            }

            val notificationManager: NotificationManager =
                activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddEditHabitBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    private var habitNameField: Boolean = false
    private var habitGoalField: Boolean = false
    private var habitUnitField: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.testingNotif?.setOnClickListener {
            scheduleNotification()
        }

        val id = navigationArgs.id
        if(id > 0) {
            // EDIT HABIT
            viewModel.getHabit(id).observe(this.viewLifecycleOwner) { selectedHabit ->
                habit = selectedHabit
                bindHabit(habit)
            }

            // HabitIcon changes when editing before saving
            viewModel.tempImageDrawable.observe(this.viewLifecycleOwner) {
                binding?.habitImage?.setImageResource(it)
            }

            viewModel.tempImageColor.observe(this.viewLifecycleOwner) {
                binding?.habitImageCard?.backgroundTintList = ColorStateList.valueOf(getColor(requireContext(), it))
            }

            // DELETE HABIT
            binding?.deleteHabitButton?.visibility = View.VISIBLE
            binding?.deleteHabitButton?.setOnClickListener {
                deleteHabit(habit)
            }
        } else {
            habitNameField = false
            habitGoalField = false
            habitUnitField = false
            // ADD HABIT
            binding?.saveAction?.setOnClickListener {
                if(binding?.itemName?.text!!.isBlank()) {
                    binding?.itemNameLabel?.isErrorEnabled = true
                    binding?.itemNameLabel?.error = getString(R.string.empty_field_error)
                } else {
                    binding?.itemNameLabel?.isErrorEnabled = false
                    habitNameField = true
                }
                if(binding?.itemGoal?.text!!.isBlank()) {
                    binding?.itemGoalLabel?.isErrorEnabled = true
                    binding?.itemGoalLabel?.error = getString(R.string.empty_field_error)
                } else {
                    binding?.itemGoalLabel?.isErrorEnabled = false
                    habitGoalField = true
                }
                if(binding?.itemUnit?.text!!.isBlank()) {
                    binding?.itemUnitLabel?.isErrorEnabled = true
                    binding?.itemUnitLabel?.error = getString(R.string.empty_field_error)
                } else {
                    binding?.itemUnitLabel?.isErrorEnabled = false
                    habitUnitField = true
                }

                if (habitNameField && habitGoalField && habitUnitField) {
                    addHabit(
                        binding!!.itemName.text.toString(),
                        binding!!.itemDescription.text.toString(),
                        viewModel.tempImageID.value!!,
                        binding!!.itemGoal.text.toString().toInt(),
                        binding!!.itemUnit.text.toString(),
                        viewModel.tempImageColorID.value!!
                    )
                }
            }
            binding?.topBar?.setTitle(R.string.add_habit_topbar_title)

            // HabitIcon changes when adding before saving
            viewModel.tempImageDrawable.observe(this.viewLifecycleOwner) {
                binding?.habitImage?.setImageResource(it)
            }

            viewModel.tempImageColor.observe(this.viewLifecycleOwner) {
                binding?.habitImageCard?.backgroundTintList = ColorStateList.valueOf(getColor(requireContext(), it))
            }
        }

        // Bind toolbar
        binding?.apply {
            topBar.inflateMenu(R.menu.top_bar_add_menu)
            topBar.setNavigationIcon(R.drawable.arrow_back)
            topBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        materialAlertDialog = MaterialAlertDialogBuilder(requireContext())

        binding?.habitImageCard?.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            HabitIconDialogFragment().show(transaction, "dialog")
        }
    }

    private fun scheduleNotification() {
        val intent = Intent(requireContext().applicationContext, AlarmNotification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            AlarmNotification.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis, pendingIntent)
    }

    private fun addHabit(name: String, description: String, icon: Int, goal: Int, unit: String, color: Int) {
        viewModel.addHabit(name, description, icon, goal, unit, false, color) // false because the new habit doesn't need to be checked
        findNavController().navigate(R.id.action_addEditHabitFragment_to_habitListFragment)
    }

    private fun updateHabit() {
        viewModel.updateHabit(
            navigationArgs.id,
            binding?.itemName?.text.toString(),
            binding?.itemDescription?.text.toString(),
            viewModel.tempImageID.value!!,
            binding?.itemGoal?.text.toString().toInt(),
            binding?.itemUnit?.text.toString(),
            habit.status,
            viewModel.tempImageColorID.value!!
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
        binding?.topBar?.setTitle(R.string.edit_habit_topbar_title)
        binding?.itemName?.setText(habit.name)
        binding?.itemDescription?.setText(habit.description)
        binding?.habitImage?.setImageResource(HabitIconsProvider.habitIconLists[habit.image].image)
        binding?.habitImageCard?.backgroundTintList = ColorStateList.valueOf(getColor(requireContext(), HabitColorsProvider.habitIconColorLists[habit.color].color))
        binding?.itemGoal?.setText(habit.goal.toString())
        binding?.itemUnit?.setText(habit.unit)
        binding?.saveAction?.setOnClickListener {
            habitNameField = false
            habitGoalField = false
            habitUnitField = false
            if(binding?.itemName?.text!!.isBlank()) {
                binding?.itemNameLabel?.isErrorEnabled = true
                binding?.itemNameLabel?.error = getString(R.string.empty_field_error)
            } else {
                binding?.itemNameLabel?.isErrorEnabled = false
                habitNameField = true
            }
            if(binding?.itemGoal?.text!!.isBlank()) {
                binding?.itemGoalLabel?.isErrorEnabled = true
                binding?.itemGoalLabel?.error = getString(R.string.empty_field_error)
            } else {
                binding?.itemGoalLabel?.isErrorEnabled = false
                habitGoalField = true
            }
            if(binding?.itemUnit?.text!!.isBlank()) {
                binding?.itemUnitLabel?.isErrorEnabled = true
                binding?.itemUnitLabel?.error = getString(R.string.empty_field_error)
            } else {
                binding?.itemUnitLabel?.isErrorEnabled = false
                habitUnitField = true
            }
            if(habitNameField && habitGoalField && habitUnitField) {
                updateHabit()
            }
        }
    }
}