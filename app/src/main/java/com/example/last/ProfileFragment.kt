package com.example.last

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Context
import android.content.Intent
import android.widget.Button

class ProfileFragment : Fragment() {

    // Inflate the fragment layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Link the layout file for the ProfileFragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the logout button
        val logoutButton: Button = view.findViewById(R.id.btn_logout)

        // Set click listener for logout
        logoutButton.setOnClickListener {
            // Show confirmation dialog
            val builder = android.app.AlertDialog.Builder(requireContext())
            builder.setTitle("Confirm Logout")
            builder.setMessage("u sure bro?")

            builder.setPositiveButton("Yes") { _, _ ->
                // Clear session data from SharedPreferences
                val sharedPreferences =
                    requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                // Redirect the user to LoginActivity
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)

                // Close the current activity to prevent going back
                requireActivity().finish()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                // Dismiss the dialog
                dialog.dismiss()
            }

            builder.create().show()
        }
    }
}
