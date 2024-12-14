package com.ecotrack.android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecotrack.android.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val colorMap = mapOf(
        "Umido" to android.graphics.Color.parseColor("#D7B19D"),
        "Indiff." to android.graphics.Color.parseColor("#E0E0E0"),
        "Plast." to android.graphics.Color.parseColor("#FFEB3B"),
        "Carta" to android.graphics.Color.parseColor("#90CAF9"),
        "Vetro" to android.graphics.Color.parseColor("#A5D6A7")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Dummy data for testing
        val timetableData = listOf(
            listOf("Tipo", "L", "M", "M", "G", "V", "S"),
            listOf("Umido", "✅", "☐", "☐", "✅", "☐", "☐"),
            listOf("Indiff.", "☐", "✅", "☐", "☐", "☐", "☐"),
            listOf("Plast.", "☐", "☐", "✅", "☐", "✅", "☐"),
            listOf("Carta", "☐", "☐", "✅", "☐", "☐", "☐"),
            listOf("Vetro", "☐", "☐", "☐", "☐", "☐", "✅")
        )

        // Set up RecyclerView
        val adapter = TimetableAdapter(timetableData, colorMap)
        binding.recyclerViewTable.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewTable.adapter = adapter


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
