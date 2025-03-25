package com.example.last

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class EmployerMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employer_main)

        // Hide the action bar
        supportActionBar?.hide()

        // Find navigation bar icons
        val addJobIcon: ImageView = findViewById(R.id.iv_add_job)
        val notificationsIcon: ImageView = findViewById(R.id.iv_notifications)
        val profileIcon: ImageView = findViewById(R.id.iv_profile)
        val homeButton: ImageView = findViewById(R.id.iv_home)

        // Debug to confirm views are initialized correctly
        Log.d("EmployerMainActivity", "Add Job Icon: $addJobIcon")
        Log.d("EmployerMainActivity", "Notifications Icon: $notificationsIcon")
        Log.d("EmployerMainActivity", "Profile Icon: $profileIcon")

        // Set up click listeners for navigation bar icons
        homeButton.setOnClickListener {
            Log.d("EmployerMainActivity", "Home button clicked")
            loadFragment(PlaceholderFragment()) // Load the home page fragment
        }

        addJobIcon.setOnClickListener {
            Log.d("EmployerMainActivity", "Add Job button clicked")
            loadFragment(AddJobFragment()) // Load Add Job fragment
        }

        notificationsIcon.setOnClickListener {
            Log.d("EmployerMainActivity", "Notifications button clicked")
            loadFragment(NotificationsFragment()) // Load Notifications fragment
        }

        profileIcon.setOnClickListener {
            Log.d("EmployerMainActivity", "Profile button clicked")
            loadFragment(ProfileFragment()) // Load Profile fragment
        }

        // Load a placeholder fragment at startup if no default fragment is set
        if (savedInstanceState == null) {
            Log.d("EmployerMainActivity", "Loading Placeholder fragment")
            loadFragment(PlaceholderFragment()) // Displays a welcome or instruction screen
        }
    }

    // Helper method to dynamically replace the fragment in the container
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)

        // Add fragment to the back stack if it's not the PlaceholderFragment
        if (fragment !is PlaceholderFragment) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
}
