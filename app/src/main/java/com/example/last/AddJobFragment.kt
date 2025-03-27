package com.example.last

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import androidx.fragment.app.activityViewModels
import com.example.last.JobPostViewModel


class AddJobFragment : Fragment() {
    private val viewModel: JobPostViewModel by activityViewModels()

    private lateinit var jobTitle: EditText
    private lateinit var companyName: EditText
    private lateinit var jobDescription: EditText
    private lateinit var salaryRange: EditText
    private lateinit var jobTypeSpinner: Spinner
    private lateinit var jobLocation: EditText
    private lateinit var submitButton: Button

    // OkHttp Client for network calls
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_job, container, false)

        // Initialize views
        jobTitle = view.findViewById(R.id.et_job_title)
        companyName = view.findViewById(R.id.et_company_name)
        jobDescription = view.findViewById(R.id.et_job_description)
        salaryRange = view.findViewById(R.id.et_salary_range)
        jobTypeSpinner = view.findViewById(R.id.spinner_job_type)
        jobLocation = view.findViewById(R.id.et_job_location)
        submitButton = view.findViewById(R.id.btn_submit_job)

        // Handle Submit Button Click
        submitButton.setOnClickListener {
            val title = jobTitle.text.toString().trim()
            val company = companyName.text.toString().trim()
            val description = jobDescription.text.toString().trim()
            val salary = salaryRange.text.toString().trim()
            val location = jobLocation.text.toString().trim()
            val jobType = jobTypeSpinner.selectedItem.toString()

            // Fetch employer_id from SharedPreferences
            val sharedPreferences = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val employerId = sharedPreferences.getString("employer_id", null)

            // Validate inputs and employer_id
            if (employerId.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Error: Employer ID not found!", Toast.LENGTH_SHORT).show()
                Log.e("AddJobFragment", "Employer ID is null or empty. Ensure it is saved during login.")
                return@setOnClickListener
            }
            if (title.isEmpty() || company.isEmpty() || description.isEmpty() || location.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all the required fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val jobPost = JobPost(
                employerId = employerId,
                jobTitle = title,
                companyName = company,
                jobDescription = description,
                salaryRange = salary,
                jobType = jobType,
                jobLocation = location
            )

            // Debugging: Log the JobPost being added
            Log.d("AddJobFragment", "JobPost being added: $jobPost") // Added this line

            // Send data to the server
            sendJobPostToServer(employerId, title, company, description, salary, jobType, location)
            viewModel.addJobPost(jobPost)
        }

        return view
    }

    private fun sendJobPostToServer(
        employerId: String,
        title: String,
        company: String,
        description: String,
        salary: String,
        jobType: String,
        location: String
    ) {
        val url = "http://10.0.2.2/api/add_job_post.php" // Update with your actual API URL
        val json = JSONObject().apply {
            put("employer_id", employerId)
            put("job_title", title)
            put("company_name", company)
            put("job_description", description)
            put("salary_range", salary)
            put("job_type", jobType)
            put("job_location", location)
        }

        // Debugging: Log the JSON payload
        Log.d("AddJobFragment", "Sending JSON: ${json.toString()}")

        val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("AddJobFragment", "Network error: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        val responseBody = it.body?.string() ?: "No Response Body"
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), "Job posted successfully!", Toast.LENGTH_SHORT).show()
                            clearFormFields()
                        }
                        Log.d("AddJobFragment", "Response Body: $responseBody")
                    } else {
                        val errorBody = it.body?.string() ?: "No Error Body"
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), "Server error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                        Log.e("AddJobFragment", "Error Body: $errorBody")
                    }
                }
            }
        })
    }

    private fun clearFormFields() {
        jobTitle.text.clear()
        companyName.text.clear()
        jobDescription.text.clear()
        salaryRange.text.clear()
        jobLocation.text.clear()
        jobTypeSpinner.setSelection(0)
    }
}
