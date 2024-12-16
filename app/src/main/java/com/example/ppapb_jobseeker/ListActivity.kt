package com.example.ppapb_jobseeker

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ppapb_jobseeker.api.RetrofitClient
import com.example.ppapb_jobseeker.api.JobRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish() // Kembali ke activity sebelumnya
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        loadJobs()
    }

    private fun loadJobs() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getJobs()
                if (response.isSuccessful) {
                    val jobs = response.body()?.map { 
                        Job(
                            id = it.id,
                            name = it.name,
                            company = it.company,
                            description = it.description,
                            requirements = it.requirements,
                            benefits = it.benefits,
                            salary = it.salary
                        )
                    } ?: emptyList()
                    
                    adapter = JobAdapter(jobs) { job ->
                        showUpdateDialog(job)
                    }
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(this@ListActivity, "Gagal memuat data pekerjaan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ListActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupListeners() {
        findViewById<FloatingActionButton>(R.id.floatingId).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
    private fun showUpdateDialog(job: Job) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        
        val edName = dialog.findViewById<EditText>(R.id.edName)
        val edCompany = dialog.findViewById<EditText>(R.id.edCompany)
        val edDescription = dialog.findViewById<EditText>(R.id.edDescription)
        val edRequirements = dialog.findViewById<EditText>(R.id.edRequirements)
        val edBenefits = dialog.findViewById<EditText>(R.id.edBenefits)
        val edSalary = dialog.findViewById<EditText>(R.id.edSalary)
        val updateBtn = dialog.findViewById<Button>(R.id.update)
        val deleteBtn = dialog.findViewById<Button>(R.id.delete)

        edName.setText(job.name)
        edCompany.setText(job.company)
        edDescription.setText(job.description)
        edRequirements.setText(job.requirements ?: "")
        edBenefits.setText(job.benefits ?: "")
        edSalary.setText(job.salary ?: "")

        updateBtn.setOnClickListener {
            if (job.id == null) {
                Toast.makeText(this@ListActivity, "Invalid job ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedJob = JobRequest(
                name = edName.text.toString().trim(),
                company = edCompany.text.toString().trim(),
                description = edDescription.text.toString().trim(),
                requirements = edRequirements.text.toString().trim(),
                benefits = edBenefits.text.toString().trim(),
                salary = edSalary.text.toString().trim()
            )
            
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.updateJob(job.id, updatedJob)
                    if (response.isSuccessful) {
                        Toast.makeText(this@ListActivity, "Berhasil mengupdate pekerjaan", Toast.LENGTH_SHORT).show()
                        loadJobs()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this@ListActivity, "Gagal mengupdate pekerjaan: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@ListActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }

        deleteBtn.setOnClickListener {
            if (job.id == null) {
                Toast.makeText(this@ListActivity, "Invalid job ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.deleteJob(job.id)
                    if (response.isSuccessful) {
                        Toast.makeText(this@ListActivity, "Berhasil menghapus pekerjaan", Toast.LENGTH_SHORT).show()
                        loadJobs()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this@ListActivity, "Gagal menghapus pekerjaan: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@ListActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }

        dialog.show()
    }
} 