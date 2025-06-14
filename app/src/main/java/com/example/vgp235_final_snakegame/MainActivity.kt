package com.example.vgp235_final_snakegame
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

// This is the main activity, serving as the game's main menu.
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        val startButton: Button = findViewById(R.id.startButton)
        val scoreboardButton: Button = findViewById(R.id.scoreboardButton)

        // Set up click listener for the Start Game button
        startButton.setOnClickListener {
            // Start the PlayerInfoActivity to get player details
            val intent = Intent(this, PlayerInfoActivity::class.java)
            startActivity(intent)
        }

        // Set up click listener for the Scoreboard button
        scoreboardButton.setOnClickListener {
            // Start the ScoreboardActivity to display high scores
            val intent = Intent(this, ScoreboardActivity::class.java)
            startActivity(intent)
        }
    }
}
