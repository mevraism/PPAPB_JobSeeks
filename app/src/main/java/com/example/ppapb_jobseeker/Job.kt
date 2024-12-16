package com.example.ppapb_jobseeker

data class Job(
    val id: String? = null,
    val name: String,
    val company: String,
    val description: String,
    val requirements: String? = null,
    val benefits: String? = null,
    val salary: String? = null
) 