package com.example.vgp235_final_snakegame
import org.json.JSONObject

// Data class to hold player information and game score.
// This will be serialized to and deserialized from JSON.
data class PlayerScore(
    val name: String,
    val age: Int,
    val country: String,
    var points: Int,
    var gameTime: Long // Game time in milliseconds
) {
    // Convert a PlayerScore object to a JSON string.
    fun toJsonString(): String {
        val jsonObject = JSONObject().apply {
            put("name", name)
            put("age", age)
            put("country", country)
            put("points", points)
            put("gameTime", gameTime)
        }
        return jsonObject.toString(4) // Use 4-space indentation for readability
    }

    companion object {
        // Create a PlayerScore object from a JSON string.
        fun fromJsonString(jsonString: String): PlayerScore {
            val jsonObject = JSONObject(jsonString)
            return PlayerScore(
                name = jsonObject.getString("name"),
                age = jsonObject.getInt("age"),
                country = jsonObject.getString("country"),
                points = jsonObject.getInt("points"),
                gameTime = jsonObject.getLong("gameTime")
            )
        }
    }
}
