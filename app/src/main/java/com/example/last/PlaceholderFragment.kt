package com.example.last

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlaceholderFragment : Fragment() {
    private val viewModel: JobPostViewModel by activityViewModels() // Shared ViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JobPostAdapter // Custom adapter for RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_placeholder, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = JobPostAdapter()
        recyclerView.adapter = adapter

        // Observe changes in the job post data from ViewModel
        viewModel.jobPosts.observe(viewLifecycleOwner) { jobList ->
            if (jobList.isEmpty()) {
                Toast.makeText(requireContext(), "No jobs to display.", Toast.LENGTH_SHORT).show()
                Log.d("PlaceholderFragment", "Job list is empty.")
            } else {
                adapter.submitList(jobList) // Update RecyclerView with new data
                Log.d("PlaceholderFragment", "Job list updated with ${jobList.size} item(s).")
            }
        }

        return view
    }
}
