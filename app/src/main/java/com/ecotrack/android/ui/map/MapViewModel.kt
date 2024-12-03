package com.ecotrack.android.ui.map

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ecotrack.android.MainActivity
import com.google.android.gms.maps.model.LatLng
import com.ecotrack.android.services.RetrofitClient
import model.Trashcan
import model.TrashcanApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Marker data object
data class MarkerData(
    val id: Long,
    val position: LatLng,
    val fillinglevel: Int,
    val trashType: String,
)

class MapViewModel : ViewModel() {

    // LiveData for markers
    private val _markers = MutableLiveData<List<MarkerData>>()
    val markers: LiveData<List<MarkerData>> get() = _markers

    // LiveData for user location
    private val _userLocation = MutableLiveData<LatLng>()
    val userLocation: LiveData<LatLng> get() = _userLocation

    // LiveData for trashcans
    private val _trashcans = MutableLiveData<List<Trashcan>>()
    val trashcans: LiveData<List<Trashcan>> get() = _trashcans


    init { // Called when the ViewModel is instantiated
        // Initialize with markers from JSON data
        /* _markers.value = listOf(
            MarkerData(id = 1L, position = LatLng(40.832848, 16.553611), fillinglevel = 95, trashType = "Plastica"),
            MarkerData(id = 2L, position = LatLng(40.828100, 16.552340), fillinglevel = 82, trashType = "Plastica"),
            MarkerData(id = 3L, position = LatLng(40.834063, 16.558287), fillinglevel = 63, trashType = "Plastica"),
            MarkerData(id = 4L, position = LatLng(40.827759, 16.549875), fillinglevel = 45, trashType = "Plastica"),
            MarkerData(id = 5L, position = LatLng(40.830221, 16.561002), fillinglevel = 67, trashType = "Plastica"),
            MarkerData(id = 6L, position = LatLng(40.824575, 16.556077), fillinglevel = 92, trashType = "Indifferenziato"),
            MarkerData(id = 7L, position = LatLng(40.829741, 16.544378), fillinglevel = 78, trashType = "Indifferenziato"),
            MarkerData(id = 8L, position = LatLng(40.824003, 16.550899), fillinglevel = 56, trashType = "Indifferenziato"),
            MarkerData(id = 9L, position = LatLng(40.831267, 16.554194), fillinglevel = 88, trashType = "Indifferenziato"),
            MarkerData(id = 10L, position = LatLng(40.826993, 16.552921), fillinglevel = 72, trashType = "Indifferenziato"),
            MarkerData(id = 11L, position = LatLng(40.835122, 16.559432), fillinglevel = 54, trashType = "Carta"),
            MarkerData(id = 12L, position = LatLng(40.830933, 16.546261), fillinglevel = 69, trashType = "Carta"),
            MarkerData(id = 13L, position = LatLng(40.827400, 16.545189), fillinglevel = 33, trashType = "Carta"),
            MarkerData(id = 14L, position = LatLng(40.832445, 16.550839), fillinglevel = 86, trashType = "Carta"),
            MarkerData(id = 15L, position = LatLng(40.824677, 16.558036), fillinglevel = 40, trashType = "Carta"),
            MarkerData(id = 16L, position = LatLng(40.832294, 16.544204), fillinglevel = 60, trashType = "Vetro"),
            MarkerData(id = 17L, position = LatLng(40.836210, 16.546822), fillinglevel = 79, trashType = "Vetro"),
            MarkerData(id = 18L, position = LatLng(40.829990, 16.547665), fillinglevel = 91, trashType = "Vetro"),
            MarkerData(id = 19L, position = LatLng(40.827181, 16.551278), fillinglevel = 58, trashType = "Vetro"),
            MarkerData(id = 20L, position = LatLng(40.835482, 16.551016), fillinglevel = 44, trashType = "Vetro")
        ) */

        _markers.value = listOf(
            MarkerData(id = 1L, position = LatLng(41.127926, 16.868977), fillinglevel = 95, trashType = "Plastica"),
            MarkerData(id = 2L, position = LatLng(41.123071, 16.869212), fillinglevel = 96, trashType = "Indifferenziato"),
            MarkerData(id = 3L, position = LatLng(41.120623, 16.881238), fillinglevel = 1, trashType = "Indifferenziato"),
            MarkerData(id = 4L, position = LatLng(41.125815, 16.868476), fillinglevel = 52, trashType = "Organico"),
            MarkerData(id = 5L, position = LatLng(41.126213, 16.868761), fillinglevel = 36, trashType = "Indifferenziato"),
            MarkerData(id = 6L, position = LatLng(41.121903, 16.871047), fillinglevel = 85, trashType = "Carta"),
            MarkerData(id = 7L, position = LatLng(41.122867, 16.868237), fillinglevel = 75, trashType = "Organico"),
            MarkerData(id = 8L, position = LatLng(41.120284, 16.868928), fillinglevel = 27, trashType = "Plastica"),
            MarkerData(id = 9L, position = LatLng(41.119913, 16.870988), fillinglevel = 39, trashType = "Plastica"),
            MarkerData(id = 10L, position = LatLng(41.121271, 16.866236), fillinglevel = 98, trashType = "Indifferenziato"),
            MarkerData(id = 11L, position = LatLng(41.119115, 16.870485), fillinglevel = 12, trashType = "Indifferenziato"),
            MarkerData(id = 12L, position = LatLng(41.120729, 16.870108), fillinglevel = 83, trashType = "Carta"),
            MarkerData(id = 13L, position = LatLng(41.117713, 16.868461), fillinglevel = 43, trashType = "Organico"),
            MarkerData(id = 14L, position = LatLng(41.115673, 16.872461), fillinglevel = 79, trashType = "Carta"),
            MarkerData(id = 15L, position = LatLng(41.118763, 16.875461), fillinglevel = 25, trashType = "Indifferenziato"),
            MarkerData(id = 16L, position = LatLng(41.121267, 16.873248), fillinglevel = 12, trashType = "Plastica"),
            MarkerData(id = 17L, position = LatLng(41.122361, 16.874831), fillinglevel = 15, trashType = "Carta"),
            MarkerData(id = 18L, position = LatLng(41.118465, 16.870159), fillinglevel = 56, trashType = "Indifferenziato"),
            MarkerData(id = 19L, position = LatLng(41.116589, 16.870384), fillinglevel = 42, trashType = "Vetro"),
            MarkerData(id = 20L, position = LatLng(41.120261, 16.876829), fillinglevel = 35, trashType = "Carta"),
            MarkerData(id = 21L, position = LatLng(41.123728, 16.870413), fillinglevel = 93, trashType = "Indifferenziato"),
            MarkerData(id = 22L, position = LatLng(41.120062, 16.866728), fillinglevel = 85, trashType = "Organico"),
            MarkerData(id = 23L, position = LatLng(41.116978, 16.867381), fillinglevel = 88, trashType = "Plastica"),
            MarkerData(id = 24L, position = LatLng(41.118932, 16.869842), fillinglevel = 29, trashType = "Indifferenziato"),
            MarkerData(id = 25L, position = LatLng(41.125111, 16.874581), fillinglevel = 44, trashType = "Organico"),
            MarkerData(id = 26L, position = LatLng(41.119832, 16.871041), fillinglevel = 22, trashType = "Carta"),
            MarkerData(id = 27L, position = LatLng(41.117895, 16.874335), fillinglevel = 32, trashType = "Carta"),
            MarkerData(id = 28L, position = LatLng(41.121587, 16.877171), fillinglevel = 13, trashType = "Indifferenziato"),
            MarkerData(id = 29L, position = LatLng(41.122893, 16.878297), fillinglevel = 56, trashType = "Organico"),
            MarkerData(id = 30L, position = LatLng(41.118682, 16.868471), fillinglevel = 38, trashType = "Carta"),
            MarkerData(id = 31L, position = LatLng(41.117465, 16.869872), fillinglevel = 88, trashType = "Plastica"),
            MarkerData(id = 32L, position = LatLng(41.119867, 16.875831), fillinglevel = 50, trashType = "Plastica"),
            MarkerData(id = 33L, position = LatLng(41.123116, 16.871542), fillinglevel = 49, trashType = "Plastica"),
            MarkerData(id = 34L, position = LatLng(41.121413, 16.866913), fillinglevel = 74, trashType = "Carta"),
            MarkerData(id = 35L, position = LatLng(41.116911, 16.871763), fillinglevel = 0, trashType = "Plastica"),
            MarkerData(id = 36L, position = LatLng(41.120348, 16.870118), fillinglevel = 30, trashType = "Indifferenziato"),
            MarkerData(id = 37L, position = LatLng(41.118231, 16.871227), fillinglevel = 22, trashType = "Carta"),
            MarkerData(id = 38L, position = LatLng(41.117846, 16.873945), fillinglevel = 96, trashType = "Plastica"),
            MarkerData(id = 39L, position = LatLng(41.115876, 16.869817), fillinglevel = 66, trashType = "Indifferenziato"),
            MarkerData(id = 40L, position = LatLng(41.119648, 16.868372), fillinglevel = 56, trashType = "Vetro"),
            MarkerData(id = 41L, position = LatLng(41.121278, 16.870935), fillinglevel = 32, trashType = "Vetro"),
            MarkerData(id = 42L, position = LatLng(41.118763, 16.872563), fillinglevel = 36, trashType = "Vetro"),
            MarkerData(id = 43L, position = LatLng(41.124012, 16.869572), fillinglevel = 93, trashType = "Carta"),
            MarkerData(id = 44L, position = LatLng(41.125372, 16.868972), fillinglevel = 36, trashType = "Organico"),
            MarkerData(id = 45L, position = LatLng(41.116972, 16.873572), fillinglevel = 80, trashType = "Indifferenziato"),
            MarkerData(id = 46L, position = LatLng(41.120285, 16.869261), fillinglevel = 16, trashType = "Indifferenziato"),
            MarkerData(id = 47L, position = LatLng(41.122847, 16.872361), fillinglevel = 77, trashType = "Carta"),
            MarkerData(id = 48L, position = LatLng(41.118749, 16.874972), fillinglevel = 40, trashType = "Organico"),
            MarkerData(id = 49L, position = LatLng(41.123186, 16.875461), fillinglevel = 14, trashType = "Indifferenziato"),
            MarkerData(id = 50L, position = LatLng(41.119483, 16.871983), fillinglevel = 19, trashType = "Carta")
        )

    }



    fun loadTrashcans() {
        val appContext = MainActivity.getAppContext() // Retrieve the application context

        // Show a toast to indicate the start of the API call
        Toast.makeText(appContext, "Loading trashcans...", Toast.LENGTH_SHORT).show()

        val apiService = RetrofitClient.instance // Instance of the Retrofit service interface TrashcanService
        apiService.getAllTrashcans().enqueue(object : Callback<TrashcanApiResponse> {

            override fun onResponse(call: Call<TrashcanApiResponse>, response: Response<TrashcanApiResponse>) {
                if (response.isSuccessful) {
                    val trashcanApiResponse = response.body()
                    val trashcans = trashcanApiResponse?.response // Access the "response" field
                    Log.d("API_SUCCESS", "Trashcans: $trashcans")

                    trashcans?.let {
                        _trashcans.value = it // Update LiveData for trashcans

                        // Update markers with trashcan data
                        val trashcanMarkers = it.map { trashcan ->
                            MarkerData(
                                id = trashcan.id,
                                position = LatLng(trashcan.latitude, trashcan.longitude),
                                fillinglevel = trashcan.fillingLevel,
                                trashType = trashcan.trashType
                            )
                        }

                        _markers.value = trashcanMarkers // Update the markers with the new trashcan data
                    }

                    // Show a toast to indicate successful data retrieval
                    Toast.makeText(appContext, "Trashcans loaded successfully!", Toast.LENGTH_SHORT).show()

                } else {
                    val errorMessage = "Error: ${response.code()} - ${response.message()}"
                    Log.e("API_ERROR", errorMessage)

                    // Show a toast to indicate an error occurred
                    Toast.makeText(appContext, "Failed to load trashcans: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<TrashcanApiResponse>, t: Throwable) {
                val failureMessage = "Failure: ${t.message}"
                Log.e("API_ERROR", failureMessage)

                // Show a toast to indicate a failure
                Toast.makeText(appContext, "Error loading trashcans: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }



    // Update user location
    fun updateUserLocation(location: LatLng) {
        _userLocation.value = location
    }

    // Add marker method
    fun addMarker(marker: MarkerData) {
        val updatedMarkers = _markers.value.orEmpty().toMutableList()
        updatedMarkers.add(marker)
        _markers.value = updatedMarkers
    }

    // Remove marker method
    fun removeMarker(marker: MarkerData) {
        val updatedMarkers = _markers.value.orEmpty().toMutableList()
        updatedMarkers.remove(marker)
        _markers.value = updatedMarkers
    }
}
