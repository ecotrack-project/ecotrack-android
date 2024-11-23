package com.ecotrack.android
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ecotrack.android.databinding.ActivityMainBinding
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
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navigation_map, R.id.navigation_home))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val apiService = RetrofitClient.instance
        apiService.getAllTrashcans().enqueue(object : Callback<List<Trashcan>> {
            override fun onResponse(call: Call<List<Trashcan>>, response: Response<List<Trashcan>>) {
                if (response.isSuccessful) {
                    val trashcans: List<Trashcan>? = response.body()
                    trashcans?.forEach { trashcan ->
                        println("TrashcanId: ${trashcan.id}, Location: (${trashcan.latitude},${trashcan.longitude}")
                    }
                } else {
                    println("Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@MainActivity, "Errore: ${response.code()}", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<List<Trashcan>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })

    }
}


