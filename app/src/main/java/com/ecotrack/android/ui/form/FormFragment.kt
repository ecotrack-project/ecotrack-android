package com.ecotrack.android.ui.form

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ecotrack.android.R
import com.google.android.material.textfield.TextInputEditText
import android.widget.Button
import androidx.core.content.ContextCompat

class FormFragment : Fragment(R.layout.fragment_form) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the trashcan ID from arguments and fill the field if available
        val trashcanId = arguments?.getInt("trashcanId", -1) // Default to -1 if not passed
        val trashcanIdField: TextInputEditText = view.findViewById(R.id.editTextTrashcanId)
        if (trashcanId != -1) {
            trashcanIdField.setText(trashcanId.toString()) // Automatically fill the field
        }

        // Find other views
        val emailEditText = view.findViewById<TextInputEditText>(R.id.editTextEmail)
        val descriptionEditText = view.findViewById<TextInputEditText>(R.id.editTextDescription)
        val submitButton = view.findViewById<Button>(R.id.submit_button)

        // Handle Submit Button Click
        submitButton.setOnClickListener {
            val userEmail = emailEditText.text?.toString()?.trim()
            val trashcanIdInput = trashcanIdField.text?.toString()?.trim()?.toLongOrNull()
            val description = descriptionEditText.text?.toString()?.trim()

            // Validate input fields
            if (userEmail.isNullOrEmpty() || trashcanIdInput == null || description.isNullOrEmpty()) {
                Toast.makeText(context, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            } else {
                // Change background color to white to indicate submission
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))

                // Simulate form submission
                Toast.makeText(
                    context,
                    "Form submitted successfully:\nEmail: $userEmail\nTrashcan ID: $trashcanIdInput\nDescription: $description",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
