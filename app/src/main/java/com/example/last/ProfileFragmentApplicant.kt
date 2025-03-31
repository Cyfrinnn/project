package com.example.last

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import android.app.AlertDialog

class ProfileFragmentApplicant : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_applicant, container, false)

        val logoutButton: Button = view.findViewById(R.id.btn_logout)
        logoutButton.setOnClickListener {
            // Show confirmation dialog
            showLogoutConfirmationDialog()
        }

        return view
    }

    private fun showLogoutConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Log Out")
            .setMessage("U SURE BRUV??")
            .setPositiveButton("Yes") { _, _ ->
                // Clear session data and redirect to LoginActivity
                val sharedPreferences = requireContext().getSharedPreferences(
                    "UserSession",
                    android.content.Context.MODE_PRIVATE
                )
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("Cancel", null) // Dismiss the dialog
            .create()

        alertDialog.show()
    }
}
