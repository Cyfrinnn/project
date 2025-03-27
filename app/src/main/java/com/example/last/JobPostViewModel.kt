package com.example.last

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log

class JobPostViewModel : ViewModel() {
    private val _jobPosts = MutableLiveData<List<JobPost>>(emptyList())
    val jobPosts: LiveData<List<JobPost>> = _jobPosts

    fun addJobPost(jobPost: JobPost) {
        val updatedList = _jobPosts.value?.toMutableList() ?: mutableListOf()
        updatedList.add(jobPost)
        _jobPosts.value = updatedList
        Log.d("JobPostViewModel", "New JobPost added: $jobPost")
    }

}
