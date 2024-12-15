package com.example.ppapb_jobseeker

import android.content.Intent
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class HomeUserActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JobUserAdapter
    private lateinit var db: AppDatabase
    private lateinit var dao: FavoriteJobDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_user)

        db = AppDatabase.getDatabase(this)
        dao = db.favoriteJobDao()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = JobUserAdapter(getJobList()) { job ->
            lifecycleScope.launch {
                val isFavorite = dao.isFavorite(job.name, job.company)
                if (isFavorite) {
                    dao.delete(job.name, job.company)
                    Toast.makeText(this@HomeUserActivity, "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    dao.insert(FavoriteJob(name = job.name, company = job.company, description = job.description))
                    Toast.makeText(this@HomeUserActivity, "Added to favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }
        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabFavorite).setOnClickListener {
            showFavorites()
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
        
        lifecycleScope.launch {
            dao.getAllFavorites().collect { favorites ->
                val jobs = favorites.map { Job(it.name, it.company, it.description) }
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

    private fun getJobList(): List<Job> {
        return listOf(
            Job("Front-End Developer", "Tiktok", "Entry Level | Full-Time | Work from Bandung, Jawa Barat"),
            Job("Back-End Developer", "Google", "Senior Level | Full-Time | Work from Bandung, Jawa Barat")
        )
    }
} 