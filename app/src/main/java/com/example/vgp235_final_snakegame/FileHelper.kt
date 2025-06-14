package com.example.vgp235_final_snakegame // IMPORTANT: Ensure this matches your actual package path

import android.content.Context
import java.io.*
import org.json.JSONException
import org.json.JSONObject // ADD THIS LINE
import org.json.JSONArray // ADD THIS LINE as well, as it's used later in the file

// Helper object to manage reading and writing player data JSON files.
object FileHelper {

    private const val SCORES_FILE_NAME = "playerscores.json"

    // Saves a single PlayerScore object to a JSON file.
    // Each player's data will be saved as a separate entry within a main JSON array.
    fun savePlayerScore(context: Context, playerScore: PlayerScore) {
        // Ensure PlayerScore is also in the correct package:
        // com.example.vgp235_final_snakegame.PlayerScore
        val allScores = loadAllPlayerScores(context).toMutableList()
        // Check if player already exists to update their score, otherwise add new player
        val existingPlayerIndex = allScores.indexOfFirst { it.name == playerScore.name }
        if (existingPlayerIndex != -1) {
            // Update existing player's score if it's higher
            if (playerScore.points > allScores[existingPlayerIndex].points) {
                allScores[existingPlayerIndex] = playerScore
            }
        } else {
            // Add new player if not found
            allScores.add(playerScore)
        }

        try {
            // Open a file output stream to write the JSON data
            context.openFileOutput(SCORES_FILE_NAME, Context.MODE_PRIVATE).use { fos ->
                val jsonArray = org.json.JSONArray() // Now org.json.JSONArray is resolved
                allScores.forEach { score ->
                    jsonArray.put(JSONObject(score.toJsonString())) // Now JSONObject is resolved
                }
                fos.write(jsonArray.toString(4).toByteArray()) // Write with 4-space indentation
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // Log or show an error to the user if saving fails
            // In a real app, you might want to display a Toast message or a dialog.
            println("Error saving player score: ${e.message}")
        }
    }

    // Loads all player scores from the JSON file.
    // Returns a list of PlayerScore objects.
    fun loadAllPlayerScores(context: Context): List<PlayerScore> {
        val scores = mutableListOf<PlayerScore>()
        try {
            // Open a file input stream to read the JSON data
            context.openFileInput(SCORES_FILE_NAME).use { fis ->
                val inputStreamReader = InputStreamReader(fis)
                val bufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder = StringBuilder()
                var line: String? = bufferedReader.readLine()
                while (line != null) {
                    stringBuilder.append(line).append("\n")
                    line = bufferedReader.readLine()
                }
                val jsonString = stringBuilder.toString()

                if (jsonString.isNotBlank()) {
                    val jsonArray = org.json.JSONArray(jsonString) // Now org.json.JSONArray is resolved
                    for (i in 0 until jsonArray.length()) {
                        val playerJson = jsonArray.getJSONObject(i).toString() // Now JSONObject is resolved
                        scores.add(PlayerScore.fromJsonString(playerJson))
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            // File not found, likely first launch, return empty list
            println("No player scores file found. Creating a new one.")
        } catch (e: IOException) {
            e.printStackTrace()
            println("Error reading player scores file: ${e.message}")
        } catch (e: JSONException) {
            e.printStackTrace()
            println("Error parsing JSON data: ${e.message}")
        }
        return scores.sortedByDescending { it.points } // Sort by points for scoreboard
    }
}
