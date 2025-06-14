package com.example.vgp235_final_snakegame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for displaying player scores in a RecyclerView.
class ScoreboardAdapter(private var scores: List<PlayerScore>) :
        RecyclerView.Adapter<ScoreboardAdapter.ScoreViewHolder>() {

        // Inner class for the ViewHolder that holds the view for each score item.
        class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val rankTextView: TextView = itemView.findViewById(R.id.rankTextView)
    val playerNameTextView: TextView = itemView.findViewById(R.id.playerNameScoreboard)
    val playerDetailsTextView: TextView = itemView.findViewById(R.id.playerDetailsScoreboard)
    val playerPointsTextView: TextView = itemView.findViewById(R.id.playerPointsScoreboard)
    val playerTimeTextView: TextView = itemView.findViewById(R.id.playerTimeScoreboard)
}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
    // Inflate the layout for a single score item
    val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.score_item_row, parent, false)
    return ScoreViewHolder(view)
}

        override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
    // Get the PlayerScore object for the current position
    val score = scores[position]

    // Bind data to the TextViews in the ViewHolder
    holder.rankTextView.text = "${position + 1}." // Display rank starting from 1
    holder.playerNameTextView.text = score.name
    holder.playerDetailsTextView.text = "Age: ${score.age}, Country: ${score.country}"
    holder.playerPointsTextView.text = "Points: ${score.points}"
    holder.playerTimeTextView.text = "Time: ${score.gameTime}s"
}

        override fun getItemCount(): Int {
    // Return the total number of scores
    return scores.size
}

        // Call this method to update the data in the adapter and refresh the RecyclerView
        fun updateScores(newScores: List<PlayerScore>) {
            scores = newScores
            notifyDataSetChanged() // Notify the adapter that the data has changed
        }
}
