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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Dummy data for testing
        val timetableData = listOf(
            listOf("Rifiuti Organici", "—", "—", "✓", "—", "—", "—"),
            listOf("Vetro e Lattine", "✓", "—", "—", "—", "—", "—"),
            listOf("Carta e Cartone", "—", "—", "—", "✓", "—", "—")
        )

        // Set up RecyclerView
        val adapter = TimetableAdapter(timetableData)
        binding.recyclerViewTable.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewTable.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
