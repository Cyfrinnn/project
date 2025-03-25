package com.example.last

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlaceholderFragment : Fragment() {

    private lateinit var jobRecyclerView: RecyclerView
    private lateinit var jobAdapter: JobAdapter
    private val jobList = mutableListOf<Job>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_placeholder, container, false)

        // Initialize RecyclerView
        jobRecyclerView = view.findViewById(R.id.rv_job_posts)
        jobAdapter = JobAdapter(jobList)
        jobRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        jobRecyclerView.adapter = jobAdapter

        // Load mock data (replace this with your backend data later)
        loadMockJobs()

        return view
    }

    private fun loadMockJobs() {
        // Add some sample job posts (replace with backend API fetching later)
        jobList.add(Job("Software Developer", "TechCorp", "Develop software solutions.", "10,000 - 15,000", "Full-time", "City A"))
        jobList.add(Job("Graphic Designer", "DesignStudio", "Create graphic designs.", "8,000 - 12,000", "Part-time", "City B"))
        jobList.add(Job("Project Manager", "ManageIT", "Manage IT projects.", "15,000 - 20,000", "Contract", "City C"))

        // Notify the adapter about data changes
        jobAdapter.notifyDataSetChanged()
    }
}
