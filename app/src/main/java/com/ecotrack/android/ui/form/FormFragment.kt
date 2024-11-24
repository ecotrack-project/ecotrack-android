package com.ecotrack.android.ui.form

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ecotrack.android.R
import com.google.android.material.textfield.TextInputEditText
import android.widget.Button

class FormFragment : Fragment(R.layout.fragment_form) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find views
        val emailEditText = view.findViewById<TextInputEditText>(R.id.editTextEmail)
        val trashcanIdEditText = view.findViewById<TextInputEditText>(R.id.editTextTrashcanId)
        val descriptionEditText = view.findViewById<TextInputEditText>(R.id.editTextDescription)
        val submitButton = view.findViewById<Button>(R.id.submitButton)

        // Handle Submit Button Click
        submitButton.setOnClickListener {
            val userEmail = emailEditText.text.toString()
            val trashcanId = trashcanIdEditText.text.toString().toLongOrNull()
            val description = descriptionEditText.text.toString()

            // Validate input
            if (userEmail.isEmpty() || trashcanId == null || description.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Change background color to white
                view.setBackgroundColor(resources.getColor(android.R.color.white, null))

                // Simulate form submission
                Toast.makeText(context, "Form submitted:\n$userEmail\n$trashcanId\n$description", Toast.LENGTH_LONG).show()
            }
        }
    }
}
