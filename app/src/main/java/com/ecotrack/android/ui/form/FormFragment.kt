package com.ecotrack.android.ui.form

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ecotrack.android.R
import com.google.android.material.textfield.TextInputEditText
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.core.view.MenuProvider
import android.view.Menu
import android.view.MenuInflater
import android.widget.TextView


class FormFragment : Fragment(R.layout.fragment_form) {

    private lateinit var formViewModel: FormViewModel

    companion object {
        private const val MIN_DESCRIPTION_LENGTH = 10
        private const val MAX_DESCRIPTION_LENGTH = 500
        private const val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        private const val DESCRIPTION_PATTERN = "^[a-zA-Z0-9\\s.,!?()-]+$"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        formViewModel = ViewModelProvider(this).get(FormViewModel::class.java)

        // Set up the back button in action bar
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // No need to create menu items
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        parentFragmentManager.popBackStack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val appCompatActivity = activity as AppCompatActivity
        //appCompatActivity.setSupportActionBar(toolbar as androidx.appcompat.widget.Toolbar?)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve the trashcan ID passed as an argument
        //val trashcanId = arguments?.getString("id")?.toLongOrNull() // Ensure it matches the argument type

        val trashcanId = arguments?.getLong("trashcanId")

        /*
        val trashcanIdField: TextInputEditText = view.findViewById(R.id.editTextTrashcanId)

        trashcanId?.let {
            trashcanIdField.setText(it.toString()) // Automatically fill the field if ID is present
            trashcanIdField.isEnabled = false // Prevent editing of the ID field
        }*/

        view.findViewById<TextInputEditText>(R.id.editTextTrashcanId)?.apply {
            isEnabled = false
            setText(trashcanId?.toString() ?: "No ID provided")
        }

        // Find input fields and button
        val emailEditText = view.findViewById<TextInputEditText>(R.id.editTextEmail)
        val descriptionEditText = view.findViewById<TextInputEditText>(R.id.editTextDescription)
        val submitButton = view.findViewById<Button>(R.id.submit_button)
        //val abortButton = view.findViewById<Button>(R.id.abort_button)

        // Set up submit button click listener
        submitButton.setOnClickListener {
            val userEmail = emailEditText.text?.toString()?.trim()
            val description = descriptionEditText.text?.toString()?.trim()

            // Validate input fields
            when {
                userEmail.isNullOrEmpty() || description.isNullOrEmpty() -> {
                    Toast.makeText(context, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
                }
                !userEmail.matches(EMAIL_PATTERN.toRegex()) -> {
                    Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                }
                description.length < MIN_DESCRIPTION_LENGTH -> {
                    Toast.makeText(context, "Description must be at least 10 characters long", Toast.LENGTH_SHORT).show()
                }
                description.length > MAX_DESCRIPTION_LENGTH -> {
                    Toast.makeText(context, "Description cannot exceed 500 characters", Toast.LENGTH_SHORT).show()
                }
                !description.matches(DESCRIPTION_PATTERN.toRegex()) -> {
                    Toast.makeText(context, "Description contains invalid characters", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Submit the form data using ViewModel
                    Toast.makeText(context, "Trashcan ID: $trashcanId", Toast.LENGTH_SHORT).show()
                    submitForm(trashcanId, userEmail, description)
                    // Navigate back in the stack
                    parentFragmentManager.popBackStack()
                }
            }

        }


        /* Set up abort button click listener
        abortButton.setOnClickListener {
            // Navigate back in the stack
            parentFragmentManager.popBackStack()
        }*/
    }



    /**
     * Submit the form data using the ViewModel.
     */
    private fun submitForm(trashcanId: Long?, email: String, description: String) {

        formViewModel.submitForm(trashcanId, email, description)

        // Observe submission status
        formViewModel.formSubmissionStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is FormSubmissionStatus.Success -> {
                    Toast.makeText(context, "Form submitted successfully!", Toast.LENGTH_SHORT).show()
                    // Optionally navigate back or reset the form
                }
                is FormSubmissionStatus.Error -> {
                    Toast.makeText(context, "Submission failed: ${status.message}", Toast.LENGTH_SHORT).show()
                }
                FormSubmissionStatus.Loading -> {
                    Toast.makeText(context, "Submitting...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

