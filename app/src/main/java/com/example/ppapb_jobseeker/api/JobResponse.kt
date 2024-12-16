package com.example.ppapb_jobseeker.api

import com.google.gson.annotations.SerializedName

data class JobResponse(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val company: String,
    val description: String,
    val requirements: String,
    val benefits: String,
    val salary: String
)

data class JobRequest(
    val name: String,
    val company: String,
    val description: String,
    val requirements: String,
    val benefits: String,
    val salary: String
) 