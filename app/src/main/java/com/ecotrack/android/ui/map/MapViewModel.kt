package com.ecotrack.android.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.ecotrack.android.services.RetrofitClient
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.Marker
import model.Trashcan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        // Initialize with predefined markers
        _markers.value = listOf(
            MarkerData(id = 1L, position = LatLng(37.422131, -122.084801), fillinglevel = 80, trashType = "Plastic"),
            MarkerData(id = 2L, position = LatLng(45.464664, 9.188540), fillinglevel = 90, trashType = "Glass"),
            MarkerData(id = 3L, position = LatLng(48.856613, 2.352222), fillinglevel = 10, trashType = "Paper")
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
















    fun updateUserLocation(location: LatLng) {
        _userLocation.value = location
    }


    fun addMarker(marker: MarkerData) {
        val updatedMarkers = _markers.value.orEmpty().toMutableList()
        updatedMarkers.add(marker)
        _markers.value = updatedMarkers
    }

    fun removeMarker(marker: MarkerData) {
        val updatedMarkers = _markers.value.orEmpty().toMutableList()
        updatedMarkers.remove(marker)
        _markers.value = updatedMarkers
    }



}
