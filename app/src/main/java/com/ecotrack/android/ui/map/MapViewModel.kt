package com.ecotrack.android.ui.map

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
            MarkerData(LatLng(37.422131, -122.084801),80, "Plastic"),
            MarkerData(LatLng(45.464664, 9.188540),90, "Glass"),
            MarkerData(LatLng(48.856613, 2.352222),10, "Paper"),
        )


    }


    fun loadTrashcans() {
        val apiService = RetrofitClient.instance // Instance of the Retrofit service interface TrashcanService
        apiService.getAllTrashcans().enqueue(object : Callback<List<Trashcan>> {

            override fun onResponse(call: Call<List<Trashcan>>, response: Response<List<Trashcan>>) {

                if (response.isSuccessful) {
                    val trashcans = response.body()

                    trashcans?.let {
                        _trashcans.value = it // Update LiveData for trashcans with the new data

                        // Update markers with trashcan data
                        val trashcanMarkers = it.map { trashcan ->
                            MarkerData(
                                position = LatLng(trashcan.latitude, trashcan.longitude),
                                fillinglevel = trashcan.fillinglevel,
                                trashType = trashcan.trashType
                            )
                        }

                        _markers.value = trashcanMarkers // We update the markers with the new trashcan data
                    }
                } else {
                    println("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Trashcan>>, t: Throwable) {
                println("Failure: ${t.message}")
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
