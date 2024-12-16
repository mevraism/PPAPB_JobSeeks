package com.example.ppapb_jobseeker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobAdapter(
    private val jobs: List<Job>,
    private val onItemClick: (Job) -> Unit
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvCompany: TextView = itemView.findViewById(R.id.tvCompany)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val updateBtn: TextView = itemView.findViewById(R.id.update)
        private val deleteBtn: TextView = itemView.findViewById(R.id.delete)

        fun bind(job: Job) {
            tvName.text = job.name
            tvCompany.text = job.company
            tvDescription.text = job.description

            updateBtn.setOnClickListener {
                onItemClick(job)
            }

            deleteBtn.setOnClickListener {
                onItemClick(job)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_item, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(jobs[position])
    }

    override fun getItemCount() = jobs.size
} 