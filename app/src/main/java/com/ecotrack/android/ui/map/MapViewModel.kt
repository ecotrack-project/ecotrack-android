package com.ecotrack.android.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.ecotrack.android.services.RetrofitClient
import model.Trashcan
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
        _markers.value = listOf(
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
        )
    }


    fun loadTrashcans() {
        val apiService = RetrofitClient.instance // Instance of the Retrofit service interface TrashcanService
        apiService.getAllTrashcans().enqueue(object : Callback<List<Trashcan>> {

            override fun onResponse(call: Call<List<Trashcan>>, response: Response<List<Trashcan>>) {

                if (response.isSuccessful) {
                    val trashcans = response.body()
                    Log.d("API_SUCCESS", "Response: $trashcans")
                    println("Response: $trashcans")

                    trashcans?.let {
                        _trashcans.value = it // Update LiveData for trashcans with the new data

                        // Update markers with trashcan data
                        val trashcanMarkers = it.map { trashcan ->
                            MarkerData(
                                id = trashcan.id,
                                position = LatLng(trashcan.latitude, trashcan.longitude),
                                fillinglevel = trashcan.fillinglevel,
                                trashType = trashcan.trashType
                            )
                        }

                        _markers.value = trashcanMarkers // We update the markers with the new trashcan data
                    }
                } else {
                    println("Error: ${response.code()} - ${response.message()}")
                    Log.e("API_ERROR", "Error: ${response.code()} - ${response.message()}")

                }
            }

            override fun onFailure(call: Call<List<Trashcan>>, t: Throwable) {
                println("Failure: ${t.message}")
                Log.e("API_ERROR", "Failure: ${t.message}")
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
