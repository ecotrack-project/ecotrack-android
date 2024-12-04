package com.ecotrack.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to the Home section. Here you can view the waste collection timetable."
    }
    val text: LiveData<String> = _text

    private val _wasteCollectionTimetable = MutableLiveData<List<List<String>>>().apply {
        // Dati di esempio: ogni sotto-lista rappresenta una riga con i giorni della settimana
        value = listOf(
            listOf("Rifiuti Organici", "—", "—", "✓", "—", "—", "—"),
            listOf("Vetro e Lattine", "✓", "—", "—", "—", "—", "—"),
            listOf("Carta e Cartone", "—", "—", "—", "✓", "—", "—")
        )
    }
    val wasteCollectionTimetable: LiveData<List<List<String>>> = _wasteCollectionTimetable

    fun updateText(newText: String) {
        _text.value = newText
    }

    fun updateTimetable(newTimetable: List<List<String>>) {
        _wasteCollectionTimetable.value = newTimetable
    }
}
