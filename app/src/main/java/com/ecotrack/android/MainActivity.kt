package com.ecotrack.android
import android.R.attr.button
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ecotrack.android.databinding.ActivityMainBinding
import com.ecotrack.android.ui.form.FormFragment
import model.Trashcan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import services.RetrofitClient



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_map, R.id.navigation_home))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val openFormButton = findViewById<Button>(R.id.openFormButton)
        // Navigate to FormFragment when button is clicked
        openFormButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FormFragment())
                .addToBackStack(null) // Adds the transaction to the back stack for back navigation
                .commit()
        }

        // Load trashcans from the server when the app starts
        loadTrashcans()


    }

    private fun loadTrashcans() {
        val apiService = RetrofitClient.instance // Instance of the Retrofit service interface TrashcanService
        apiService.getAllTrashcans().enqueue(object : Callback<List<Trashcan>> {
            override fun onResponse(call: Call<List<Trashcan>>, response: Response<List<Trashcan>>) {
                if (response.isSuccessful) {
                    val trashcans = response.body()
                    trashcans?.forEach { trashcan ->
                        println("TrashcanId: ${trashcan.id}, Location: (${trashcan.latitude}, ${trashcan.longitude})")
                    }
                } else {
                    println("Error: ${response.code()} - ${response.message()}")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Trashcan>>, t: Throwable) {
                println("Failure: ${t.message}")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to load trashcans", Toast.LENGTH_LONG).show()
                }
            }
        })
    }





}


