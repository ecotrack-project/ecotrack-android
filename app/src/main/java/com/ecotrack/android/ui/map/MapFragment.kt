package com.ecotrack.android.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ecotrack.android.MarkerDetailsFragment
import com.ecotrack.android.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions





class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var viewModel: MapViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val DEFAULT_ZOOM_LEVEL = 15f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        GoogleMapOptions().mapId(resources.getString(R.string.map_id))



        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        // Initialize MapView and ViewModel
        mapView = rootView.findViewById(R.id.mapView)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Set up the MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return rootView
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Check and request location permissions
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        // Set the OnMarkerClickListener
        googleMap?.setOnMarkerClickListener { marker ->
            handleMarkerClick(marker)
            true // Return true to consume the event
        }

        // Observe ViewModel data for markers and trashcans
        observeMarkers()
        observeTrashcans()


    }

    @SuppressLint("MissingPermission")
    private fun enableUserLocation() {
        // Enable user location on the map
        googleMap?.isMyLocationEnabled = true

        // Move the camera to the user's location if available
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val userLatLng = LatLng(it.latitude, it.longitude)

                // Update ViewModel with user location
                viewModel.updateUserLocation(userLatLng)

                // Center camera on user location
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_ZOOM_LEVEL))
            } ?: Toast.makeText(context, "User location unavailable", Toast.LENGTH_SHORT).show()
        }
    }


    private val markerDataMap = mutableMapOf<Marker, MarkerData>()

    private fun observeMarkers() {
        viewModel.markers.observe(viewLifecycleOwner) { markers ->
            googleMap?.clear() // Clear existing markers
            markerDataMap.clear() // Clear existing associations

            markers.forEach { markerData ->
                googleMap?.addMarker(
                    MarkerOptions().position(markerData.position)
                )?.let { marker ->
                    markerDataMap[marker] = markerData // Associate marker with its data
                }
            }
        }
    }


    private fun observeTrashcans() {
        // Observe trashcan data from ViewModel
        viewModel.trashcans.observe(viewLifecycleOwner) { trashcans ->
            trashcans.forEach { trashcan ->
                // Add a marker for each trashcan
                val position = LatLng(trashcan.latitude, trashcan.longitude)
                googleMap?.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title("Trashcan ID: ${trashcan.id}")
                )
            }
        }

        // Trigger data loading in ViewModel
        viewModel.loadTrashcans()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            enableUserLocation()
        } else {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleMarkerClick(marker: Marker) {
        val markerData = markerDataMap[marker]
        markerData?.let {
            val dialogFragment = MarkerDetailsFragment.newInstance(it.id, it.trashType, it.fillinglevel)
            dialogFragment.show(parentFragmentManager, "MarkerDetailsFragment")
        }
    }





    // Handle lifecycle events for MapView
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
