package com.ecotrack.android.ui.home
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ecotrack.android.R
import com.ecotrack.android.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        setupRecyclerView()
        observeViewModel()

        // Set title directly since it's static
        binding.textNotifications.text = getString(R.string.WasteCollectionTimetable)

        return binding.root
    }


    private fun setupRecyclerView() {
        binding.recyclerViewTable.apply {
            layoutManager = LinearLayoutManager(context)
            // Optional: Add item decoration for spacing between items
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }


    private fun observeViewModel() {
        homeViewModel.timetableData.observe(viewLifecycleOwner) { timetableData ->
            binding.recyclerViewTable.adapter = TimetableAdapter(
                timetableData,
                HomeViewModel.WASTE_COLOR_MAP
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}