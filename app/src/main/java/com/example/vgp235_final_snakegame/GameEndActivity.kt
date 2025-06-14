package com.example.vgp235_final_snakegame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
// Make sure MainActivity is correctly imported.
// Assuming MainActivity is in the same package, no explicit import might be needed if not in a subpackage.
// If MainActivity is in a different package (e.g., com.example.vgp235_final_snakegame.main), you'd need:
// import com.example.vgp235_final_snakegame.main.MainActivity

// Activity to display game over or victory screen.
class GameEndActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_end)

        // Initialize UI components
        val gameStatusTextView: TextView = findViewById(R.id.gameStatusTextView)
        val finalScoreTextView: TextView = findViewById(R.id.finalScoreTextView)
        val finalTimeTextView: TextView = findViewById(R.id.finalTimeTextView)
        val backToMainMenuButton: Button = findViewById(R.id.backToMainMenuButton)

        // Retrieve game results from the Intent
        val finalScore = intent.getIntExtra("finalScore", 0)
        val finalGameTime = intent.getLongExtra("finalGameTime", 0)
        val isVictory = intent.getBooleanExtra("isVictory", false)

        // Update UI based on game outcome
        if (isVictory) {
            gameStatusTextView.text = getString(R.string.game_status_victory) // Using string resource
            gameStatusTextView.setTextColor(resources.getColor(R.color.colorPrimary, theme))
        } else {
            gameStatusTextView.text = getString(R.string.game_status_game_over) // Using string resource
            gameStatusTextView.setTextColor(resources.getColor(android.R.color.holo_red_dark, theme))
        }

        // Using string resources with placeholders for score and time
        finalScoreTextView.text = getString(R.string.final_score_format, finalScore)
        finalTimeTextView.text = getString(R.string.time_played_format, finalGameTime)

        backToMainMenuButton.setOnClickListener {
            // Navigate back to the main menu (MainActivity)
            // Ensure MainActivity is correctly referenced here.
            // If MainActivity is in a different package, you might need to use its fully qualified name:
            // val intent = Intent(this, com.example.vgp235_final_snakegame.main.MainActivity::class.java)
            val intent = Intent(this, MainActivity::class.java) // Assuming MainActivity is in the same package
            // Clear activity stack to prevent going back to game end screen
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}