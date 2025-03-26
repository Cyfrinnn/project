package com.example.last
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JobPostViewModel : ViewModel() {
    // MutableLiveData to hold the list of job posts
    val jobPosts = MutableLiveData<MutableList<JobPost>>(mutableListOf())
}

// Data model for JobPost
data class JobPost(
    val employerId: String,
    val jobTitle: String,
    val companyName: String,
    val jobDescription: String,
    val salaryRange: String,
    val jobType: String,
    val jobLocation: String
)
