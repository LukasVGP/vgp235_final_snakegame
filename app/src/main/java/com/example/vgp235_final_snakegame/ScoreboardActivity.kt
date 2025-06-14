package com.example.vgp235_final_snakegame

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Activity to display the scoreboard (high scores).
class ScoreboardActivity : AppCompatActivity() {

    private lateinit var scoreboardRecyclerView: RecyclerView
    private lateinit var backToMenuButton: Button
    private lateinit var scoreboardAdapter: ScoreboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

        // Initialize UI components
        scoreboardRecyclerView = findViewById(R.id.scoreboardRecyclerView)
        backToMenuButton = findViewById(R.id.backToMenuButton)

        // Set up RecyclerView
        scoreboardRecyclerView.layoutManager = LinearLayoutManager(this)
        scoreboardAdapter = ScoreboardAdapter(emptyList()) // Initialize with an empty list
        scoreboardRecyclerView.adapter = scoreboardAdapter

        // Load and display scores when the activity is created
        loadScores()

        backToMenuButton.setOnClickListener {
            finish() // Go back to MainActivity
        }
    }

    // Loads all player scores using the FileHelper and updates the RecyclerView.
    private fun loadScores() {
        val scores = FileHelper.loadAllPlayerScores(this)
        scoreboardAdapter.updateScores(scores)
    }
}
