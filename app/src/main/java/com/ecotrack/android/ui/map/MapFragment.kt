package com.ecotrack.android.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ecotrack.android.MarkerDetailsFragment
import com.ecotrack.android.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.android.volley.Request
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.google.maps.android.PolyUtil
import org.json.JSONException
import org.json.JSONObject


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var viewModel: MapViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null

    // User position
    private var userPos: LatLng? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val DEFAULT_ZOOM_LEVEL = 15f
    }

    // Create MapView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        GoogleMapOptions().mapId(resources.getString(R.string.map_id))

        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        // Initialize MapView and ViewModel
        mapView = rootView.findViewById(R.id.mapView)
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Set MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return rootView
    }


    // Create Google Map
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Check and request location permissions
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation()
        } else {
            //requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Set the OnMarkerClickListener
        googleMap?.setOnMarkerClickListener { marker -> handleMarkerClick(marker)
            true // Return true to consume the event
        }

        // Trigger data loading in ViewModel
        viewModel.loadTrashcans()

        //observeTrashcans()
        // Observe ViewModel data for markers and trashcans
        observeMarkers()

    }

    @SuppressLint("MissingPermission")
    private fun enableUserLocation() {
        // Enable user location on the map
        googleMap?.isMyLocationEnabled = true

        // Move the camera to the user's location if available
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                //val userLatLng = LatLng(it.latitude, it.longitude)
                val userLatLng = LatLng(45.06365, 7.66030)

                this.userPos = userLatLng

                viewModel.updateUserLocation(userLatLng)

                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_ZOOM_LEVEL))
            } ?: Toast.makeText(context, "Posizione non disponibile", Toast.LENGTH_SHORT).show()
        }
    }

    // Markers
    private val markerDataMap = mutableMapOf<Marker, MarkerData>()

    // Add markers method
    private fun observeMarkers() {
        viewModel.markers.observe(viewLifecycleOwner) { markers ->
            googleMap?.clear()
            markerDataMap.clear()

            markers.forEach { markerData ->
                val iconUrl = when (markerData.trashType) {
                    "Plastica" -> R.drawable.plastica
                    "Indifferenziato" -> R.drawable.indifferenziato
                    "Carta" -> R.drawable.carta
                    "Vetro" -> R.drawable.vetro
                    "Organico" -> R.drawable.organico
                    else -> R.drawable.indifferenziato
                }

                val originalIcon = BitmapFactory.decodeResource(resources, iconUrl)

                val scaledIcon = Bitmap.createScaledBitmap(originalIcon, 120, 120, false)

                val icon = BitmapDescriptorFactory.fromBitmap(scaledIcon)

                googleMap?.addMarker(
                    MarkerOptions().position(markerData.position).title("Tipo: ${markerData.trashType}").icon(icon))?.let { marker ->
                    markerDataMap[marker] = markerData
                }
            }
        }
    }


    // Marker view
    private fun observeTrashcans() {
        // Observe trashcan data from ViewModel
        viewModel.trashcans.observe(viewLifecycleOwner) { trashcans ->
            trashcans.forEach { trashcan ->
                // Add a marker for each trashcan
                val position = LatLng(trashcan.latitude, trashcan.longitude)
                googleMap?.addMarker(MarkerOptions().position(position).title("Trashcan ID: ${trashcan.id}"))
            }
        }
    }



    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            enableUserLocation()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }




    // Variabile globale per la polilinea
    private var currentPolyline: Polyline? = null

    fun calculateRoute(latitude: Double?, longitude: Double?) {

        val origin = userPos?.let { LatLng(it.latitude, it.longitude) }

        // Google Maps API
        val apiKey = resources.getString(R.string.google_maps_api_key)

        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=${origin?.latitude},${origin?.longitude}" +
                "&destination=${latitude},${longitude}" +
                "&key=$apiKey"

        // Request
        val requestQueue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                parseDirectionsResponse(response)
            },
            { _ ->
                Toast.makeText(context, "Error computing the route!", Toast.LENGTH_SHORT).show()
            })
        requestQueue.add(request)
    }

    // Parse route
    private fun parseDirectionsResponse(response: String) {
        try {
            val jsonObject = JSONObject(response)
            val status = jsonObject.getString("status")
            if (status != "OK") {
                Toast.makeText(context, "Errore nel calcolo della rotta: $status", Toast.LENGTH_SHORT).show()
                return
            }

            // Estrai la rotta
            val routes = jsonObject.getJSONArray("routes")
            if (routes.length() > 0) {
                val route = routes.getJSONObject(0)
                val polyline = route.getJSONObject("overview_polyline").getString("points")
                val decodedPath = PolyUtil.decode(polyline)

                // Remove previous
                currentPolyline?.remove()

                // Add new
                val polylineOptions = PolylineOptions().addAll(decodedPath).color(Color.BLUE).width(10f)
                currentPolyline = googleMap?.addPolyline(polylineOptions)

                //userPos?.let { moveCameraToUserPosition(it) };

            } else {
                Toast.makeText(context, "Nessuna rotta trovata", Toast.LENGTH_SHORT).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Toast.makeText(context, "Errore nell'analisi della risposta", Toast.LENGTH_SHORT).show()
        }
    }



    // Move camera on user
    private fun moveCameraToUserPosition(position: LatLng) {
        val zoomLevel = 15f
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel))
    }


    // Position
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            enableUserLocation()
        } else {
            Toast.makeText(context, "Errore geolocalizzazione", Toast.LENGTH_SHORT).show()
        }
    }

    // Method for click on marker
    private fun handleMarkerClick(marker: Marker) {
        val markerData = markerDataMap[marker]
        markerData?.let {
            val dialogFragment = MarkerDetailsFragment.newInstance(
                it.id, it.trashType, it.fillinglevel, it.position.latitude, it.position.longitude
            )
            dialogFragment.show(parentFragmentManager, "MarkerDetailsFragment")
        }
    }





    // Default methods for lifecycle of MapView
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
