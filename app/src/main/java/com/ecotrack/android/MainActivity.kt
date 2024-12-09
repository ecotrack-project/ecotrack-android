package com.ecotrack.android

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.ecotrack.android.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView



/**
 * @MainActivity for the application
 */
class MainActivity : AppCompatActivity() {


    /**
     * Companion object to provide global access to the application context
     */
    companion object {

        private lateinit var instance: MainActivity

        fun getAppContext(): Context {
            return instance.applicationContext
        }
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        instance = this // Set the instance to the current activity

        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the BottomNavigationView
        val navView: BottomNavigationView = binding.navView

        // Set up navigation controller
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_map,
            R.id.navigation_home
        )) // Define top-level destinations

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Handle navigation selection to ensure FormFragment is closed when clicking map
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_map -> {
                    // Navigate to MapFragment if not already there
                    if (navController.currentDestination?.id != R.id.navigation_map) {
                        navController.popBackStack(R.id.navigation_map, false)
                    }
                    true
                }
                R.id.navigation_home -> {
                    // Navigate to HomeFragment if not already there
                    navController.navigate(R.id.navigation_home)
                    true
                }
                else -> false
            }
        }
    }
}
