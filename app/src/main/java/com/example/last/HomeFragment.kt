package com.example.last

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class HomeFragment : Fragment() {

    private lateinit var jobPostAdapterApplicant: JobPostAdapterApplicant
    private val jobPostList = mutableListOf<JobPost>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_jobs_home)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        jobPostAdapterApplicant = JobPostAdapterApplicant(jobPostList)
        recyclerView.adapter = jobPostAdapterApplicant

        // Fetch job data
        fetchJobs()

        return view
    }

    private fun fetchJobs() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://10.0.2.2/api/fetch_jobs.php") // Ensure this matches your API endpoint
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Log the failure
                Log.e("FetchJobsError", "Network request failed: ${e.message}")
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Failed to load jobs. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseBody = response.body?.string()
                    Log.d("FetchJobsResponse", "Raw response: $responseBody") // Log the raw response

                    if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                        val jsonArray = JSONArray(responseBody)

                        if (jsonArray.length() == 0) {
                            activity?.runOnUiThread {
                                Toast.makeText(requireContext(), "No jobs found.", Toast.LENGTH_SHORT).show()
                            }
                            return
                        }

                        // Parse JSON and populate the list
                        for (i in 0 until jsonArray.length()) {
                            val jobObject = jsonArray.getJSONObject(i)
                            val jobPost = JobPost(
                                employerId = jobObject.optString("employer_id", "N/A"),
                                jobTitle = jobObject.optString("job_title", "N/A"),
                                companyName = jobObject.optString("company_name", "N/A"),
                                jobDescription = jobObject.optString("job_description", "N/A"),
                                salaryRange = jobObject.optString("salary_range", "N/A"),
                                jobType = jobObject.optString("job_type", "N/A"),
                                jobLocation = jobObject.optString("job_location", "N/A")
                            )
                            jobPostList.add(jobPost)
                        }

                        // Update the UI on the main thread
                        activity?.runOnUiThread {
                            jobPostAdapterApplicant.notifyDataSetChanged()
                        }
                    } else {
                        Log.e("FetchJobsResponse", "Server error: ${response.code}")
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "Server error: ${response.code}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("FetchJobsError", "Error processing response: ${e.message}")
                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), "Error loading jobs. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
