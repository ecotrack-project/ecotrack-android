package com.ecotrack.android.ui.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FormViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to the Form section! Here you can report waste collection."
    }
    val text: LiveData<String> = _text

    /**
     * Updates the value of the text LiveData.
     * @param newText The new text to be set.
     */
    fun updateText(newText: String) {
        _text.value = newText
    }
}
