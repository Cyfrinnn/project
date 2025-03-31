package com.example.last

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobPostAdapterApplicant(private val jobPostList: List<JobPost>) : RecyclerView.Adapter<JobPostAdapterApplicant.JobPostViewHolder>() {

    class JobPostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val jobTitle: TextView = view.findViewById(R.id.tv_job_title_applicant)
        val companyName: TextView = view.findViewById(R.id.tv_company_name_applicant)
        val jobDescription: TextView = view.findViewById(R.id.tv_job_description_applicant)
        val salaryRange: TextView = view.findViewById(R.id.tv_salary_range_applicant)
        val jobType: TextView = view.findViewById(R.id.tv_job_type_applicant)
        val jobLocation: TextView = view.findViewById(R.id.tv_job_location_applicant)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobPostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job_post_applicant, parent, false)
        return JobPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobPostViewHolder, position: Int) {
        val jobPost = jobPostList[position]
        holder.jobTitle.text = jobPost.jobTitle
        holder.companyName.text = jobPost.companyName
        holder.jobDescription.text = jobPost.jobDescription
        holder.salaryRange.text = jobPost.salaryRange
        holder.jobType.text = jobPost.jobType
        holder.jobLocation.text = jobPost.jobLocation
    }

    override fun getItemCount(): Int = jobPostList.size
}
