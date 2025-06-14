package com.example.vgp235_final_snakegame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Activity to get player name, age, and country.
class PlayerInfoActivity : AppCompatActivity() {

    private lateinit var playerNameEditText: EditText
    private lateinit var playerAgeEditText: EditText
    private lateinit var playerCountryEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var backToMenuButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_info)

        // Initialize UI components
        playerNameEditText = findViewById(R.id.playerNameEditText)
        playerAgeEditText = findViewById(R.id.playerAgeEditText)
        playerCountryEditText = findViewById(R.id.playerCountryEditText)
        saveButton = findViewById(R.id.saveButton)
        backToMenuButton = findViewById(R.id.backToMenuButton)

        saveButton.setOnClickListener {
            // Get player input
            val name = playerNameEditText.text.toString().trim()
            val ageString = playerAgeEditText.text.toString().trim()
            val country = playerCountryEditText.text.toString().trim()

            // Validate input
            if (name.isEmpty() || ageString.isEmpty() || country.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageString.toIntOrNull()
            if (age == null || age <= 0) {
                Toast.makeText(this, "Please enter a valid age!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a PlayerScore object with initial values for a new game
            val currentPlayer = PlayerScore(name, age, country, 0, 0) // Points and time start at 0

            // Start the GameActivity and pass player data
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra("playerName", currentPlayer.name)
                putExtra("playerAge", currentPlayer.age)
                putExtra("playerCountry", currentPlayer.country)
            }
            startActivity(intent)
            finish() // Finish this activity so pressing back returns to main menu
        }

        backToMenuButton.setOnClickListener {
            finish() // Go back to MainActivity
        }
    }
}
