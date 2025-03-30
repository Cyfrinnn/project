package com.example.last

import android.content.Context
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
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class PlaceholderFragment : Fragment() {
    private val viewModel: JobPostViewModel by activityViewModels() // Shared ViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JobPostAdapter // Custom adapter for RecyclerView
    private val client = OkHttpClient()

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

        // Load cached jobs when app starts
        loadJobsFromPreferences()?.let { cachedJobs ->
            adapter.submitList(cachedJobs.toMutableList()) // Use mutable list for appending
            Log.d("PlaceholderFragment", "Loaded cached jobs: ${cachedJobs.size}")
        }

        // Fetch jobs from the server and merge with cached data
        fetchJobs()

        // Observe changes in the job post data from ViewModel
        viewModel.jobPosts.observe(viewLifecycleOwner) { jobList ->
            if (jobList.isEmpty()) {
                Toast.makeText(requireContext(), "No jobs to display.", Toast.LENGTH_SHORT).show()
                Log.d("PlaceholderFragment", "Job list is empty.")
            } else {
                adapter.submitList(jobList.toMutableList()) // Update RecyclerView without overwriting
                saveJobsToPreferences(jobList) // Cache the job list locally
                Log.d("PlaceholderFragment", "Job list updated with ${jobList.size} item(s).")
            }
        }

        return view
    }

    // Function to fetch jobs and merge them with cached data
    private fun fetchJobs() {
        val request = Request.Builder()
            .url("http://10.0.2.2/api/fetch_jobs.php") // Replace with your actual URL
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Failed to fetch jobs: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val responseString = responseBody.string()

                    // Safeguard: Check for valid JSON response
                    if (!responseString.trim().startsWith("[") || !responseString.trim().endsWith("]")) {
                        Log.e("PlaceholderFragment", "Invalid server response: $responseString")
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), "Invalid data received from server", Toast.LENGTH_SHORT).show()
                        }
                        return
                    }

                    val fetchedJobList = ArrayList<JobPost>()

                    try {
                        val jobs = JSONArray(responseString)
                        for (i in 0 until jobs.length()) {
                            val job = jobs.getJSONObject(i)
                            fetchedJobList.add(
                                JobPost(
                                    employerId = job.getString("employer_id"),
                                    jobTitle = job.getString("job_title"),
                                    companyName = job.getString("company_name"),
                                    jobLocation = job.getString("job_location"),
                                    jobType = job.getString("job_type"),
                                    salaryRange = job.getString("salary_range"),
                                    jobDescription = job.getString("job_description")
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("PlaceholderFragment", "Error parsing server response: ${e.message}")
                    }

                    // Merge fetched jobs with cached jobs
                    val cachedJobs = loadJobsFromPreferences() ?: ArrayList()
                    val mergedJobList = ArrayList<JobPost>()
                    mergedJobList.addAll(cachedJobs)

                    for (fetchedJob in fetchedJobList) {
                        if (!mergedJobList.any { it.jobTitle == fetchedJob.jobTitle }) { // Avoid duplicates
                            mergedJobList.add(fetchedJob)
                        }
                    }

                    // Update UI and cache
                    requireActivity().runOnUiThread {
                        adapter.submitList(mergedJobList.toMutableList()) // Use mutable list for appending
                    }
                    saveJobsToPreferences(mergedJobList) // Save merged list locally
                    Log.d("PlaceholderFragment", "Jobs updated and cached.")
                }
            }
        })
    }

    // Function to save jobs to Shared Preferences
    private fun saveJobsToPreferences(jobList: List<JobPost>) {
        val sharedPreferences = requireContext().getSharedPreferences("JobCache", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert job list to JSON
        val jobArray = JSONArray()
        jobList.forEach { job ->
            val jobObject = JSONObject()
            jobObject.put("employerId", job.employerId)
            jobObject.put("jobTitle", job.jobTitle)
            jobObject.put("companyName", job.companyName)
            jobObject.put("jobLocation", job.jobLocation)
            jobObject.put("jobType", job.jobType)
            jobObject.put("salaryRange", job.salaryRange)
            jobObject.put("jobDescription", job.jobDescription)
            jobArray.put(jobObject)
        }

        editor.putString("CachedJobs", jobArray.toString())
        editor.apply() // Save data to Shared Preferences
        Log.d("PlaceholderFragment", "Jobs cached locally.")
    }

    // Function to load cached jobs from Shared Preferences
    private fun loadJobsFromPreferences(): List<JobPost>? {
        val sharedPreferences = requireContext().getSharedPreferences("JobCache", Context.MODE_PRIVATE)
        val cachedData = sharedPreferences.getString("CachedJobs", null)
        return if (cachedData != null) {
            val jobArray = JSONArray(cachedData)
            val jobList = ArrayList<JobPost>()
            for (i in 0 until jobArray.length()) {
                val jobObject = jobArray.getJSONObject(i)
                jobList.add(
                    JobPost(
                        employerId = jobObject.getString("employerId"),
                        jobTitle = jobObject.getString("jobTitle"),
                        companyName = jobObject.getString("companyName"),
                        jobLocation = jobObject.getString("jobLocation"),
                        jobType = jobObject.getString("jobType"),
                        salaryRange = jobObject.getString("salaryRange"),
                        jobDescription = jobObject.getString("jobDescription")
                    )
                )
            }
            jobList
        } else {
            null // No cached data found
        }
    }
}
