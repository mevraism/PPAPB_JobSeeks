package com.example.ppapb_jobseeker

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoriteAdapter(
    private val jobs: List<Job>,
    private val onRemoveClick: (Job) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvCompany: TextView = itemView.findViewById(R.id.tvCompany)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val detailBtn: TextView = itemView.findViewById(R.id.detail)

        fun bind(job: Job) {
            tvName.text = job.name
            tvCompany.text = job.company
            tvDescription.text = job.description

            detailBtn.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, JobDetailActivity::class.java).apply {
                    putExtra("job_name", job.name)
                    putExtra("company", job.company)
                    putExtra("description", job.description)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_item, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(jobs[position])
    }

    override fun getItemCount() = jobs.size
} 