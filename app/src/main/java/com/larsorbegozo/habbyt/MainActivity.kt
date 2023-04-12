package com.larsorbegozo.habbyt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.larsorbegozo.habbyt.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.larsorbegozo.habbyt.ui.habits.AddEditHabitFragment
import com.larsorbegozo.habbyt.ui.mood.AddEditMoodFragment
import com.larsorbegozo.habbyt.ui.habits.DetailHabitFragment
import com.larsorbegozo.habbyt.ui.mood.DetailMoodFragment

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
                // This is used for the transition TODO: Investigate
                TransitionManager.beginDelayedTransition(binding.root, Slide(Gravity.BOTTOM).excludeTarget(R.id.nav_host_fragment, true))
                when (f) {
                    is AddEditHabitFragment -> {
                        binding.bottomBarNavigation.visibility = View.INVISIBLE
                        binding.bottomBar.visibility = View.INVISIBLE
                        binding.addHabitFab.visibility = View.INVISIBLE
                    }
                    is DetailHabitFragment -> {
                        binding.bottomBarNavigation.visibility = View.INVISIBLE
                        binding.bottomBar.visibility = View.INVISIBLE
                        binding.addHabitFab.visibility = View.INVISIBLE
                    }
                    is AddEditMoodFragment -> {
                        binding.bottomBarNavigation.visibility = View.INVISIBLE
                        binding.bottomBar.visibility = View.INVISIBLE
                        binding.addHabitFab.visibility = View.INVISIBLE
                    }
                    is DetailMoodFragment -> {
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