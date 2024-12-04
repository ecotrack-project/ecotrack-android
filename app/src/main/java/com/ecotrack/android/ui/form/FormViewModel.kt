package com.ecotrack.android.ui.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FormViewModel : ViewModel() {

    // LiveData per mostrare un messaggio nella UI
    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to the Form section! Here you can report waste collection."
    }
    val text: LiveData<String> = _text


    // LiveData per tracciare lo stato della sottomissione del modulo
    private val _formSubmissionStatus = MutableLiveData<FormSubmissionStatus>()
    val formSubmissionStatus: LiveData<FormSubmissionStatus> = _formSubmissionStatus

    /**
     * Updates the welcome text LiveData.
     * @param newText The new text to be set.
     */
    fun updateText(newText: String) {
        _text.value = newText
    }

    /**
     * Submits the form data to the backend.
     * @param trashcanId The ID of the trashcan.
     * @param email The user's email address.
     * @param description The report description.
     */
    fun submitForm(trashcanId: Long?, email: String, description: String) {
        // Simula una chiamata al backend (o usa una vera API)
        _formSubmissionStatus.value = FormSubmissionStatus.Loading

        viewModelScope.launch {
            try {
                // Simula un'operazione di invio al backend
                simulateBackendCall(trashcanId, email, description)

                // Aggiorna lo stato a Success dopo l'invio
                _formSubmissionStatus.value = FormSubmissionStatus.Success
            } catch (e: Exception) {
                // Aggiorna lo stato a Error se qualcosa va storto
                _formSubmissionStatus.value = FormSubmissionStatus.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Simulates a backend API call.
     * Replace this with actual Retrofit or API service calls.
     */
    private suspend fun simulateBackendCall(trashcanId: Long?, email: String, description: String) {
        // Simulazione di una chiamata API con ritardo
        kotlinx.coroutines.delay(2000) // Simula una chiamata che richiede 2 secondi
        if (trashcanId.toString().isBlank() || email.isBlank() || description.isBlank()) {
            throw IllegalArgumentException("Invalid input data")
        }
    }



}

/**
 * Represents the status of the form submission process.
 */
sealed class FormSubmissionStatus {
    object Loading : FormSubmissionStatus()
    object Success : FormSubmissionStatus()
    data class Error(val message: String) : FormSubmissionStatus()
}
