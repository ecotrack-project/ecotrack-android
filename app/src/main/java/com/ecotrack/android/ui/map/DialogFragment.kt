package com.ecotrack.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.ecotrack.android.ui.map.MapFragment

class MarkerDetailsFragment : DialogFragment() {
    private var trashcanId: Long? = null // Updated to Long
    private var trashType: String? = null
    private var fillinglevel: Int? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    companion object {
        fun newInstance(trashcanId: Long, trashType: String, fillinglevel: Int, latitude: Double, longitude: Double): MarkerDetailsFragment {
            val fragment = MarkerDetailsFragment()
            val args = Bundle()
            args.putLong("trashcanId", trashcanId)
            args.putString("trashType", trashType)
            args.putInt("fillinglevel", fillinglevel)
            args.putDouble("latitude", latitude)
            args.putDouble("longitude", longitude)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure trashcanId matches the Long type
        trashcanId = arguments?.getLong("trashcanId", -1L)
        trashType = arguments?.getString("trashType")
        fillinglevel = arguments?.getInt("fillinglevel")
        latitude = arguments?.getDouble("latitude")
        longitude = arguments?.getDouble("longitude")
        setStyle(STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_marker_details, container, false)

        val trashTypeTextView: TextView = view.findViewById(R.id.trashTypeTextView)
        val fillingLevelTextView: TextView = view.findViewById(R.id.fillingLevelTextView)
        val closeButton: ImageView = view.findViewById(R.id.closeButton)
        val findPathButton: Button = view.findViewById(R.id.find_path)
        val sendReportButton: Button = view.findViewById(R.id.send_report)

        trashTypeTextView.text = trashType
        fillingLevelTextView.text = "Livello di riempimento: $fillinglevel%"

        closeButton.setOnClickListener {
            dismiss()
        }

        // Import method from Fragment
        findPathButton.setOnClickListener {
            val fragments = parentFragmentManager.fragments
            val mapFragment = fragments.find { it is MapFragment } as? MapFragment
            mapFragment?.calculateRoute(latitude, longitude) ?: run {
                Toast.makeText(requireContext(), "Errore nel calcolo rotta", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }

        // Handle Send Report button click
        sendReportButton.setOnClickListener {
            if (trashcanId == null || trashcanId == -1L) {
                //Toast.makeText(requireContext(), R.string.error_invalid_trashcan_id, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bundle = Bundle().apply { putLong("trashcanId", trashcanId!!) }
            findNavController().navigate(R.id.action_markerDetails_to_formFragment, bundle)
            dismiss()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * 0.85).toInt(), // Set width to 85% of screen width
                ViewGroup.LayoutParams.WRAP_CONTENT // Adjust height dynamically
            )
            clearFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND) // Remove background dim
        }
    }
}
