package com.ecotrack.android.ui.form

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

class FormFragment : Fragment(R.layout.fragment_form) {

    private lateinit var formViewModel: FormViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        formViewModel = ViewModelProvider(this).get(FormViewModel::class.java)

        val appCompatActivity = activity as AppCompatActivity
        //appCompatActivity.setSupportActionBar(toolbar as androidx.appcompat.widget.Toolbar?)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve the trashcan ID passed as an argument
        //val trashcanId = arguments?.getString("id")?.toLongOrNull() // Ensure it matches the argument type


        val trashcanId = arguments?.getLong("trashcanId")

        val trashcanIdField: TextInputEditText = view.findViewById(R.id.editTextTrashcanId)
        trashcanId?.let {
            trashcanIdField.setText(it.toString()) // Automatically fill the field if ID is present
            trashcanIdField.isEnabled = false // Prevent editing of the ID field
        }

        // Find input fields and button
        val emailEditText = view.findViewById<TextInputEditText>(R.id.editTextEmail)
        val descriptionEditText = view.findViewById<TextInputEditText>(R.id.editTextDescription)
        val submitButton = view.findViewById<Button>(R.id.submit_button)
        val abortButton = view.findViewById<Button>(R.id.abort_button)

        // Set up submit button click listener
        submitButton.setOnClickListener {
            val userEmail = emailEditText.text?.toString()?.trim()
            val description = descriptionEditText.text?.toString()?.trim()

            // Validate input fields
            if (userEmail.isNullOrEmpty() || description.isNullOrEmpty()) {
                Toast.makeText(context, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            } else {
                // Submit the form data using ViewModel
                Toast.makeText(context, "Trashcan ID: $trashcanId", Toast.LENGTH_SHORT).show()
                submitForm(trashcanId, userEmail, description)
                // Navigate back in the stack
                parentFragmentManager.popBackStack()

            }
        }

        // Set up abort button click listener
        abortButton.setOnClickListener {
            // Navigate back in the stack
            parentFragmentManager.popBackStack()
        }


    }




    // NEW  NOT SERVE NOW ONLY IF I FOUND TEH TOOLBAR BUTTON
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Navigate back in the stack
                parentFragmentManager.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
