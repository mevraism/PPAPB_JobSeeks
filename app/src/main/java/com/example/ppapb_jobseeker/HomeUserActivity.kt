package com.example.ppapb_jobseeker

import android.content.Intent
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.example.ppapb_jobseeker.api.RetrofitClient
import kotlinx.coroutines.launch

class HomeUserActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JobUserAdapter
    private lateinit var dao: FavoriteJobDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_user)

        dao = AppDatabase.getDatabase(this).favoriteJobDao()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        loadJobs()

        findViewById<FloatingActionButton>(R.id.fabFavorite).setOnClickListener {
            showFavorites()
        }

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
                    
                    adapter = JobUserAdapter(jobs) { job ->
                        lifecycleScope.launch {
                            val isFavorite = dao.isFavorite(job.name, job.company)
                            if (isFavorite) {
                                dao.delete(job.name, job.company)
                                Toast.makeText(this@HomeUserActivity, "Dihapus dari favorit", Toast.LENGTH_SHORT).show()
                            } else {
                                dao.insert(FavoriteJob(name = job.name, company = job.company, description = job.description))
                                Toast.makeText(this@HomeUserActivity, "Ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(this@HomeUserActivity, "Gagal memuat data: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@HomeUserActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun showFavorites() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_favorites)
        
        val window = dialog.window
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvFavorites)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        dialog.findViewById<Button>(R.id.btnClearAll).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Clear Favorites")
                .setMessage("Are you sure want to clear all favorites?")
                .setPositiveButton("Yes") { _, _ ->
                    lifecycleScope.launch {
                        dao.deleteAll()
                        Toast.makeText(this@HomeUserActivity, "All favorites cleared", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
        
        lifecycleScope.launch {
            dao.getAllFavorites().collect { favorites ->
                val jobs = favorites.map { 
                    Job(
                        id = null,
                        name = it.name,
                        company = it.company,
                        description = it.description,
                        requirements = null,
                        benefits = null,
                        salary = null
                    )
                }
                val adapter = FavoriteAdapter(jobs) { job ->
                    lifecycleScope.launch {
                        dao.delete(job.name, job.company)
                        Toast.makeText(this@HomeUserActivity, "Removed from favorites", Toast.LENGTH_SHORT).show()
                    }
                }
                recyclerView.adapter = adapter
            }
        }

        dialog.show()
    }
} 