package com.ecotrack.android.ui.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecotrack.android.services.RetrofitClient
import kotlinx.coroutines.launch
import model.Report

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


    fun submitForm(trashcanId: Long?, email: String, description: String) {
        // Verifica input
        if (trashcanId == null || trashcanId <= 0 || email.isBlank() || description.isBlank()) {
            _formSubmissionStatus.value = FormSubmissionStatus.Error("Invalid input")
            return
        }

        _formSubmissionStatus.value = FormSubmissionStatus.Loading

        viewModelScope.launch {
            try {
                // Crea l'oggetto Report da inviare
                val report = Report(
                    email = email,
                    trashcanId = trashcanId,
                    description = description
                )
                // Simula un'operazione di invio al backend
                //simulateBackendCall(trashcanId, email, description)
                // Chiamata reale al backend tramite Retrofit
                val response = RetrofitClient.reportService.createReport(report)

                // Controlla la risposta del backend
                if (response.isSuccessful) {
                    _formSubmissionStatus.value = FormSubmissionStatus.Success
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown server error"
                    _formSubmissionStatus.value = FormSubmissionStatus.Error("Server error: $errorMessage")
                }
            } catch (e: Exception) {
                // Gestione di errori generici (es. timeout, connessione)
                _formSubmissionStatus.value = FormSubmissionStatus.Error(e.message ?: "Connection error")
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
