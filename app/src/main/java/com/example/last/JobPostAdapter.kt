package com.example.last

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.last.JobPost

class JobPostAdapter : ListAdapter<JobPost, JobPostAdapter.JobPostViewHolder>(DiffCallback()) {

    // ViewHolder class
    class JobPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_job_title)
        private val companyTextView: TextView = itemView.findViewById(R.id.tv_company_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tv_job_description)
        private val salaryTextView: TextView = itemView.findViewById(R.id.tv_salary_range)
        private val jobTypeTextView: TextView = itemView.findViewById(R.id.tv_job_type)
        private val locationTextView: TextView = itemView.findViewById(R.id.tv_job_location)

        fun bind(jobPost: JobPost) {
            titleTextView.text = jobPost.jobTitle
            companyTextView.text = jobPost.companyName
            descriptionTextView.text = jobPost.jobDescription
            salaryTextView.text = jobPost.salaryRange
            jobTypeTextView.text = jobPost.jobType
            locationTextView.text = jobPost.jobLocation
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobPostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_job_post, parent, false)
        return JobPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobPostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // DiffUtil for efficient list updates
    class DiffCallback : DiffUtil.ItemCallback<JobPost>() {
        override fun areItemsTheSame(oldItem: JobPost, newItem: JobPost): Boolean {
            return oldItem.jobTitle == newItem.jobTitle // You may want to use a unique identifier like job ID
        }

        override fun areContentsTheSame(oldItem: JobPost, newItem: JobPost): Boolean {
            return oldItem == newItem
        }
    }
}
