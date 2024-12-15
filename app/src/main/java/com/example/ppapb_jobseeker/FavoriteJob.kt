package com.example.ppapb_jobseeker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_jobs")
data class FavoriteJob(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val company: String,
    val description: String
) 