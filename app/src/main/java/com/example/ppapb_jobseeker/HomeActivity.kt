package com.example.ppapb_jobseeker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.ppapb_jobseeker.api.JobRequest
import com.example.ppapb_jobseeker.api.RetrofitClient
import kotlinx.coroutines.launch
import com.example.ppapb_jobseeker.MainActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var edName: EditText
    private lateinit var edCompany: EditText
    private lateinit var edDescription: EditText
    private lateinit var edRequirements: EditText
    private lateinit var edBenefits: EditText
    private lateinit var edSalary: EditText
    private lateinit var addBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        initViews()
        setupListeners()

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya") { _, _ ->
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Tidak", null)
                .show()
        }
    }

    private fun initViews() {
        edName = findViewById(R.id.edName)
        edCompany = findViewById(R.id.edCompany)
        edDescription = findViewById(R.id.edDescription)
        edRequirements = findViewById(R.id.edRequirements)
        edBenefits = findViewById(R.id.edBenefits)
        edSalary = findViewById(R.id.edSalary)
        addBtn = findViewById(R.id.addBtn)
    }

    private fun setupListeners() {
        addBtn.setOnClickListener {
            createJob()
        }

        findViewById<FloatingActionButton>(R.id.floatingId).setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }
    }

    private fun createJob() {
        val jobRequest = JobRequest(
            name = edName.text.toString(),
            company = edCompany.text.toString(),
            description = edDescription.text.toString(),
            requirements = edRequirements.text.toString(),
            benefits = edBenefits.text.toString(),
            salary = edSalary.text.toString()
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.createJob(jobRequest)
                if (response.isSuccessful) {
                    Toast.makeText(this@HomeActivity, "Job created successfully", Toast.LENGTH_SHORT).show()
                    clearInputs()
                } else {
                    Toast.makeText(this@HomeActivity, "Failed to create job", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearInputs() {
        edName.text.clear()
        edCompany.text.clear()
        edDescription.text.clear()
        edRequirements.text.clear()
        edBenefits.text.clear()
        edSalary.text.clear()
    }
} 