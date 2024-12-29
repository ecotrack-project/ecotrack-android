// HomeViewModel.kt
package com.ecotrack.android.ui.home

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    companion object {
        val WASTE_COLOR_MAP = mapOf(
            "Umido" to Color.parseColor("#D7B19D"),
            "Indiff." to Color.parseColor("#E0E0E0"),
            "Plast." to Color.parseColor("#FFEB3B"),
            "Carta" to Color.parseColor("#90CAF9"),
            "Vetro" to Color.parseColor("#A5D6A7")
        )

        private val DEFAULT_TIMETABLE = listOf(
            listOf("Tipo", "L", "M", "M", "G", "V", "S"),
            listOf("Umido", "✅", "☐", "☐", "✅", "☐", "☐"),
            listOf("Indiff.", "☐", "✅", "☐", "☐", "☐", "☐"),
            listOf("Plast.", "☐", "☐", "✅", "☐", "✅", "☐"),
            listOf("Carta", "☐", "☐", "✅", "☐", "☐", "☐"),
            listOf("Vetro", "☐", "☐", "☐", "☐", "☐", "✅")
        )
    }

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to the Home section. Here you can view the waste collection timetable."
    }
    val text: LiveData<String> = _text

    private val _timetableData = MutableLiveData<List<List<String>>>().apply {
        value = DEFAULT_TIMETABLE
    }
    val timetableData: LiveData<List<List<String>>> = _timetableData

    fun updateText(newText: String) {
        _text.value = newText
    }

    fun updateTimetable(newTimetable: List<List<String>>) {
        _timetableData.value = newTimetable
    }
}
