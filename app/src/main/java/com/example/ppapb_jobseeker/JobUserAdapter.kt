package com.example.ppapb_jobseeker

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobUserAdapter(
    private val jobs: List<Job>,
    private val onFavoriteClick: (Job) -> Unit
) : RecyclerView.Adapter<JobUserAdapter.JobViewHolder>() {

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvCompany: TextView = itemView.findViewById(R.id.tvCompany)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val detailBtn: TextView = itemView.findViewById(R.id.detail)
        private val favoriteBtn: TextView = itemView.findViewById(R.id.favorite)

        fun bind(job: Job) {
            tvName.text = job.name
            tvCompany.text = job.company
            tvDescription.text = job.description

            detailBtn.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, JobDetailActivity::class.java).apply {
                    putExtra("job_id", job.id)
                }
                context.startActivity(intent)
            }

            favoriteBtn.setOnClickListener {
                onFavoriteClick(job)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_item_user, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(jobs[position])
    }

    override fun getItemCount() = jobs.size
} 