package com.ecotrack.android

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ecotrack.android.databinding.ActivityMainBinding
import com.ecotrack.android.ui.form.FormFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the BottomNavigationView
        val navView: BottomNavigationView = binding.navView

        // Set up navigation controller
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_map,
            R.id.navigation_home
        )) // Define top-level destinations
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Handle navigation to FormFragment when button is clicked
        val openFormButton: Button? = findViewById(R.id.openFormButton)
        openFormButton?.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, FormFragment()) // Corrected container ID
                .addToBackStack(null) // Adds to back stack for proper navigation
                .commit()
        }
    }
}
