package com.example.ppapb_jobseeker.api

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("jobs")
    suspend fun getJobs(): Response<List<JobResponse>>
    
    @POST("jobs")
    suspend fun createJob(@Body jobRequest: JobRequest): Response<JobResponse>
    
    @PUT("jobs/{id}")
    suspend fun updateJob(
        @Path("id") id: String,
        @Body jobRequest: JobRequest
    ): Response<JobResponse>
    
    @DELETE("jobs/{id}")
    suspend fun deleteJob(@Path("id") id: String): Response<Unit>
} 