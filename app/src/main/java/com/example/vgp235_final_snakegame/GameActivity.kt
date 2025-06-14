package com.example.vgp235_final_snakegame

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Activity that hosts the Snake game.
class GameActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var scoreTextView: TextView
    private var playerName: String = "Guest"
    private var playerAge: Int = 0
    private var playerCountry: String = "Unknown"

    private var gameStartTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())
    private val gameLoopRunnable = object : Runnable {
        override fun run() {
            if (gameView.isGameRunning) {
                gameView.update()
                updateScoreAndTime()
                handler.postDelayed(this, gameView.gameSpeed.toLong())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Retrieve player data passed from PlayerInfoActivity
        playerName = intent.getStringExtra("playerName") ?: "Guest"
        playerAge = intent.getIntExtra("playerAge", 0)
        playerCountry = intent.getStringExtra("playerCountry") ?: "Unknown"

        scoreTextView = findViewById(R.id.scoreTextView)
        gameView = findViewById(R.id.gameView)

        // Pass player details to the GameView
        gameView.setPlayerDetails(playerName, playerAge, playerCountry)

        // Set up game over listener from GameView
        gameView.onGameOverListener = { finalScore, finalGameTime, isVictory ->
            // Save player score
            val playerScore = PlayerScore(playerName, playerAge, playerCountry, finalScore, finalGameTime)
            FileHelper.savePlayerScore(this, playerScore)

            // Navigate to GameEndActivity
            val intent = Intent(this, GameEndActivity::class.java).apply {
                putExtra("finalScore", finalScore)
                putExtra("finalGameTime", finalGameTime)
                putExtra("isVictory", isVictory)
            }
            startActivity(intent)
            finish() // Finish GameActivity so user can't go back to it
        }

        startGameLoop()
    }

    // Starts the game loop using a Handler.
    private fun startGameLoop() {
        gameStartTime = System.currentTimeMillis()
        gameView.startGame() // Reset game state in GameView
        handler.post(gameLoopRunnable)
    }

    // Updates the score and time displayed on the screen.
    private fun updateScoreAndTime() {
        val elapsedTime = (System.currentTimeMillis() - gameStartTime) / 1000 // in seconds
        scoreTextView.text = "Score: ${gameView.currentScore} | Time: ${elapsedTime}s"
    }

    override fun onPause() {
        super.onPause()
        gameView.pauseGame() // Pause game when activity is not in foreground
        handler.removeCallbacks(gameLoopRunnable)
    }

    override fun onResume() {
        super.onResume()
        if (gameView.isGameRunning) { // Only resume if game was running before pause
            handler.post(gameLoopRunnable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(gameLoopRunnable) // Stop game loop
    }
}
