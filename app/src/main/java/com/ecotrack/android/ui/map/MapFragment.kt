package com.ecotrack.android.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.privacysandbox.tools.core.model.Method
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
import com.android.volley.Response
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.PolyUtil
import org.json.JSONException
import org.json.JSONObject

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var viewModel: MapViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null

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

                this.userPos = userLatLng

                // Update ViewModel with user location
                viewModel.updateUserLocation(userLatLng)

                // Center camera on user location
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_ZOOM_LEVEL))
            } ?: Toast.makeText(context, "User location unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    // Markers
    private val markerDataMap = mutableMapOf<Marker, MarkerData>()

    private fun observeMarkers() {
        viewModel.markers.observe(viewLifecycleOwner) { markers ->
            googleMap?.clear() // Clear existing markers
            markerDataMap.clear() // Clear existing associations

            markers.forEach { markerData ->
                val iconColor = when (markerData.trashType) {
                    "Plastica" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW) // Blu per Plastica
                    "Indifferenziato" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA) // Grigio scuro per Indifferenziato
                    "Carta" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE) // Giallo per Carta
                    "Vetro" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN) // Verde per Vetro
                    "Organico" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE) // Marrone per Organico
                    else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET) // Viola come colore predefinito
                }

                // Aggiungi il marker con l'icona corrispondente
                googleMap?.addMarker(
                    MarkerOptions()
                        .position(markerData.position)
                        .title("Tipo: ${markerData.trashType}")
                        .icon(iconColor) // Usa il colore dell'icona
                )?.let { marker ->
                    markerDataMap[marker] = markerData // Associa il marker con i suoi dati
                }
            }
        }
    }


    // Scaricamento dati rotta da Google Maps
    fun calculateRoute() {
        if (markerDataMap.size < 2) return // Se ci sono meno di 2 marker, non possiamo calcolare la rotta

        val origin = LatLng(40.832848, 16.553611)  // Esempio di coordinate di origine
        val destination = LatLng(40.830221, 16.561002)  // Esempio di coordinate di destinazione


        // Prepara i waypoints
        // Prepara i waypoints escludendo il primo e l'ultimo MarkerData dalla mappa
        val waypoints = markerDataMap.values
            .drop(1)  // Rimuove il primo MarkerData
            .dropLast(1)  // Rimuove l'ultimo MarkerData
            .joinToString("|") {
                "${it.position.latitude},${it.position.longitude}"  // Formatta latitudine e longitudine
            }


        // URL per la richiesta della rotta
        val apiKey=resources.getString(R.string.google_maps_api_key)
        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=${origin?.latitude},${origin?.longitude}" +
                "&destination=${destination.latitude},${destination?.longitude}" +
                "&waypoints=optimize:true|$waypoints" +
                "&key=$apiKey"

        // Esegui la richiesta
        val requestQueue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                parseDirectionsResponse(response)
            },
            { _ ->
                Toast.makeText(context, "Errore nel calcolo della rotta", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(request)
    }


    // PARSE METHOD
    private fun parseDirectionsResponse(response: String) {
        try {
            val jsonObject = JSONObject(response)
            val status = jsonObject.getString("status")
            if (status != "OK") {
                Log.e("DirectionsAPI", "Errore nella risposta: $status")
                Toast.makeText(context, "Errore nel calcolo della rotta: $status", Toast.LENGTH_SHORT).show()
                return
            }

            // Estrai la rotta
            val routes = jsonObject.getJSONArray("routes")
            if (routes.length() > 0) {
                val route = routes.getJSONObject(0)

                // Estrai la polyline
                val polyline = route.getJSONObject("overview_polyline").getString("points")

                // Log per vedere la polyline prima della decodifica
                Log.d("Polyline", "Polyline string: $polyline")

                // Decodifica la polyline in una lista di LatLng
                val decodedPath = PolyUtil.decode(polyline)

                // Log per vedere il percorso decodificato
                Log.d("DecodedPolyline", "Decoded path: $decodedPath")

                // Aggiungi la polilinea alla mappa
                val polylineOptions = PolylineOptions().addAll(decodedPath).color(Color.BLUE).width(10f)
                googleMap?.addPolyline(polylineOptions)

                // (Opzionale) Aggiorna la visualizzazione della mappa per includere l'intero percorso
                val bounds = LatLngBounds.builder()
                for (latLng in decodedPath) {
                    bounds.include(latLng)
                }
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))

            } else {
                Toast.makeText(context, "Nessuna rotta trovata", Toast.LENGTH_SHORT).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Toast.makeText(context, "Errore nel parsing della risposta", Toast.LENGTH_SHORT).show()
        }
    }



    // Marker view
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

    // Position
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
            val dialogFragment = MarkerDetailsFragment.newInstance(it.id, it.trashType, it.fillinglevel)
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
