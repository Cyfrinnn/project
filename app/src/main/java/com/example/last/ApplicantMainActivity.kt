package com.example.last

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ApplicantMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applicant_main)

        // Load the default fragment (HomeFragment)
        loadFragment(HomeFragment())

        // Set up navigation bar buttons
        val homeButton: ImageView = findViewById(R.id.iv_home)
        val searchButton: ImageView = findViewById(R.id.iv_search) // Now a Search button
        val notificationButton: ImageView = findViewById(R.id.iv_notifications)
        val profileButton: ImageView = findViewById(R.id.iv_profile)

        homeButton.setOnClickListener { loadFragment(HomeFragment()) }
        searchButton.setOnClickListener { loadFragment(SearchFragment()) }
        notificationButton.setOnClickListener { loadFragment(NotificationsFragmentApplicant()) }
        profileButton.setOnClickListener { loadFragment(ProfileFragmentApplicant()) }
    }

    private fun loadFragment(fragment: Fragment) {
        // Replace the current fragment in the FrameLayout
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
