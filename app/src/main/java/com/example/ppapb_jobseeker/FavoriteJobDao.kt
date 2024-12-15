package com.example.ppapb_jobseeker

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteJobDao {
    @Query("SELECT * FROM favorite_jobs")
    fun getAllFavorites(): Flow<List<FavoriteJob>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: FavoriteJob)

    @Query("DELETE FROM favorite_jobs WHERE name = :name AND company = :company")
    suspend fun delete(name: String, company: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_jobs WHERE name = :name AND company = :company)")
    suspend fun isFavorite(name: String, company: String): Boolean
} 