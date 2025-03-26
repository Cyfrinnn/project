package com.example.last

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.last.JobPostViewModel

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
        recyclerView = view.findViewById(R.id.recycler_view) // Ensure this matches your layout ID
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = JobPostAdapter() // Initialize your custom adapter
        recyclerView.adapter = adapter

        // Observe changes in the job post data
        viewModel.jobPosts.observe(viewLifecycleOwner) { jobList ->
            if (jobList.isEmpty()) {
                Toast.makeText(requireContext(), "No jobs to display.", Toast.LENGTH_SHORT).show()
            } else {
                adapter.submitList(jobList) // Update RecyclerView with new data
            }
        }

        return view
    }
}
