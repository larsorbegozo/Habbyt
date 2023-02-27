package com.larsorbegozo.habbyt

import android.app.appsearch.observer.ObserverSpec
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.habbyt.R
import com.example.habbyt.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.larsorbegozo.habbyt.model.Habit
import com.larsorbegozo.habbyt.ui.AchievementFragment
import com.larsorbegozo.habbyt.ui.AddEditHabitFragment
import com.larsorbegozo.habbyt.ui.HabitListFragment
import com.larsorbegozo.habbyt.ui.viewmodel.HabitViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_bar_navigation)
        setupWithNavController(bottomNavigationView, navController)

        // This allows to have the BottomAppBar (FAB embedded) with BottomNavigationView
        binding.bottomBarNavigation.background = null
        // This allows to have the FAB more separated for the rest of the items is BottomNavigationView
        binding.bottomBarNavigation.menu.getItem(2).isEnabled = false

        // Top bar with back button?
        setSupportActionBar(findViewById(R.id.top_bar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Hide bottomNavigationView, FAB & BottomAppBar when necessary
        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
                TransitionManager.beginDelayedTransition(binding.root, Slide(Gravity.BOTTOM).excludeTarget(R.id.nav_host_fragment, true))
                when (f) {
                    is AddEditHabitFragment -> {
                        binding.bottomBarNavigation.visibility = View.INVISIBLE
                        binding.bottomBar.visibility = View.INVISIBLE
                        binding.addHabitFab.visibility = View.INVISIBLE
                    }
                    else -> {
                        binding.bottomBarNavigation.visibility = View.VISIBLE
                        binding.bottomBar.visibility = View.VISIBLE
                        binding.addHabitFab.visibility = View.VISIBLE
                    }
                }
            }
        }, true)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}