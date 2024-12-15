package com.example.ppapb_jobseeker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class JobDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        // Ambil data dari intent
        val jobName = intent.getStringExtra("job_name")
        val company = intent.getStringExtra("company")
        val description = intent.getStringExtra("description")

        // Set data ke TextView
        findViewById<TextView>(R.id.tvJobName).text = jobName
        findViewById<TextView>(R.id.tvCompanyName).text = company
        findViewById<TextView>(R.id.tvJobDescription).text = description
        findViewById<TextView>(R.id.tvRequirements).text = "Pendidikan minimal S1 di bidang informatika atau sejenisnya, pengalaman kerja minimal 2 tahun, menguasai JavaScript, React, dan Next.js, mampu bekerja dengan metode Agile Scrum, serta memiliki kemampuan berbahasa Inggris aktif."
        findViewById<TextView>(R.id.tvBenefits).text = "Asuransi kesehatan, kebijakan Work From Home (WFH), bonus performa, dan peluang pengembangan karier di lingkungan kerja yang inovatif."
        findViewById<TextView>(R.id.tvSalary).text = "Rp 8.000.000 - Rp 12.000.000"
    }
} 