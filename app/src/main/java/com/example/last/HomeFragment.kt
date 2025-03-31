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
            .url("http://10.0.2.2/api/fetch_jobs.php") // Replace with your backend API endpoint
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FetchJobsError", e.message.toString())
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Failed to load jobs", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                    val jsonArray = JSONArray(responseBody)

                    // Parse JSON and update the list
                    for (i in 0 until jsonArray.length()) {
                        val jobObject = jsonArray.getJSONObject(i)
                        val jobPost = JobPost(
                            employerId = jobObject.getString("employer_id"),
                            jobTitle = jobObject.getString("job_title"),
                            companyName = jobObject.getString("company_name"),
                            jobDescription = jobObject.getString("job_description"),
                            salaryRange = jobObject.getString("salary_range"),
                            jobType = jobObject.getString("job_type"),
                            jobLocation = jobObject.getString("job_location")
                        )
                        jobPostList.add(jobPost)
                    }

                    // Update the UI on the main thread
                    activity?.runOnUiThread {
                        jobPostAdapterApplicant.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
