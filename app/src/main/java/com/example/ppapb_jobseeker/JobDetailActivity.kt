package com.example.ppapb_jobseeker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.ppapb_jobseeker.api.RetrofitClient
import com.example.ppapb_jobseeker.api.JobResponse
import kotlinx.coroutines.launch

class JobDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }

        val jobId = intent.getStringExtra("job_id")
        if (jobId != null) {
            loadJobDetails(jobId)
        } else {
            displayJobFromIntent()
        }
    }

    private fun loadJobDetails(jobId: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getJobs()
                if (response.isSuccessful) {
                    val job = response.body()?.find { it.id == jobId }
                    if (job != null) {
                        displayJob(job)
                    } else {
                        Toast.makeText(this@JobDetailActivity, "Pekerjaan tidak ditemukan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@JobDetailActivity, "Gagal memuat detail pekerjaan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@JobDetailActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun displayJob(job: JobResponse) {
        findViewById<TextView>(R.id.tvJobName).text = job.name
        findViewById<TextView>(R.id.tvCompanyName).text = job.company
        findViewById<TextView>(R.id.tvJobDescription).text = job.description
        findViewById<TextView>(R.id.tvRequirements).text = job.requirements
        findViewById<TextView>(R.id.tvBenefits).text = job.benefits
        findViewById<TextView>(R.id.tvSalary).text = if (!job.salary.startsWith("Rp")) "Rp ${job.salary}" else job.salary
    }

    private fun displayJobFromIntent() {
        // Untuk menampilkan data dari favorite jobs
        findViewById<TextView>(R.id.tvJobName).text = intent.getStringExtra("job_name")
        findViewById<TextView>(R.id.tvCompanyName).text = intent.getStringExtra("company")
        findViewById<TextView>(R.id.tvJobDescription).text = intent.getStringExtra("description")
        // Tampilkan pesan default untuk data yang tidak tersedia
        findViewById<TextView>(R.id.tvRequirements).text = "Requirements data not available"
        findViewById<TextView>(R.id.tvBenefits).text = "Benefits data not available"
        findViewById<TextView>(R.id.tvSalary).text = "Salary data not available"
    }
} 