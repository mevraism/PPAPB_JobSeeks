package com.example.ppapb_jobseeker

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // Inisialisasi adapter (akan dibuat selanjutnya)
        adapter = JobAdapter(getJobList()) { job ->
            showUpdateDialog(job)
        }
        recyclerView.adapter = adapter
    }

    private fun showUpdateDialog(job: Job) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog)
        
        // Inisialisasi views dalam dialog
        val edName = dialog.findViewById<EditText>(R.id.edName)
        val edCompany = dialog.findViewById<EditText>(R.id.edCompany)
        val edDescription = dialog.findViewById<EditText>(R.id.edDescription)
        val updateBtn = dialog.findViewById<Button>(R.id.customBtn)

        // Set data yang ada
        edName.setText(job.name)
        edCompany.setText(job.company)
        edDescription.setText(job.description)

        updateBtn.setOnClickListener {
            // Handle update logic here
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getJobList(): List<Job> {
        // Contoh data dummy
        return listOf(
            Job("Front-End Developer", "Tiktok", "Fresh-Graduate w/ Experience in React, Vue, Angular, and/or NextJS"),
            Job("Back-End Developer", "Google", "Experience with Java, Kotlin, and Spring Boot")
        )
    }
} 