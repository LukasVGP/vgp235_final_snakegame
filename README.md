
🐍 Android Snake Game 🍎
A classic Snake game reimagined for Android, featuring intuitive touch controls, score tracking, player profiles, and a persistent scoreboard.

Table of Contents
About the Project
Features
Screenshots
Technologies Used
Architecture
Getting Started
Prerequisites
Installation
How to Play
Future Enhancements
Contributing
License
Author
About the Project
This project implements a classic Snake game for Android devices. Players control a snake on a grid, aiming to eat food to grow longer and earn points, while avoiding collisions with walls or its own body. The game tracks scores, game time, and allows players to input their details (name, age, country), which are then saved to a persistent scoreboard.

The application is built entirely in Kotlin, leveraging Android's core components like Activities, custom Views, and RecyclerViews for a smooth user experience.

Features
Classic Snake Gameplay: Engage in the timeless challenge of growing your snake and avoiding obstacles.
Intuitive Touch Controls: Change snake direction by tapping on the screen relative to the snake's head. Prevents instant 180-degree turns.
Dynamic Difficulty: Game speed increases incrementally as the snake consumes food.
Score and Time Tracking: Real-time display of current score and elapsed game time during gameplay.
Randomized Food Spawning: Food appears at random unoccupied locations, with a strategic prioritization to spawn on the snake's current row for a smoother gameplay experience.
Varied Food Visuals: Food items are rendered as random shapes (square, circle, triangle) with distinct colors.
Collision Detection: Ends the game upon collision with the board boundaries or the snake's own body.
Victory Condition: Reach a predefined snake length (e.g., 15 segments) to achieve victory!
Player Profiles: Input player name, age, and country before starting a game.
Persistent Scoreboard: All game results are saved locally and displayed in a sortable (implied, though not explicitly sorted in the provided ScoreboardAdapter logic) scoreboard, retaining high scores across app launches.
Clear Game States: Dedicated screens for the main menu, player info entry, active gameplay, and game end results.
Screenshots
(Placeholder: Add screenshots of your app's main menu, gameplay, player info, game end, and scoreboard screens here.)

Main Menu	Gameplay	Scoreboard

Export to Sheets
Technologies Used
Kotlin: The primary programming language for Android development.
Android SDK: Core Android framework for building applications.
AndroidX Libraries:
androidx.appcompat.app.AppCompatActivity: Base class for activities.
androidx.recyclerview.widget.RecyclerView: Efficiently displays lists of data for the scoreboard.
Android Canvas API: Used in GameView for custom 2D drawing of the game board, snake, and food.
JSON (org.json.JSONObject): Used for serializing and deserializing PlayerScore objects for local persistence.
Android Handler and Looper: For managing the game loop and ensuring UI updates on the main thread.
android.util.Log: For debugging and logging game events.
Architecture
The application follows a standard Android Activity-based architecture with a clear separation of concerns:

MainActivity: Serves as the application's entry point and the main menu. It handles navigation to other key sections of the app.
PlayerInfoActivity: A dedicated screen for capturing player details (name, age, country) before a game begins. It validates input and passes data to the game session.
GameActivity: The central controller for the game session. It hosts the GameView, manages the game loop (timing updates), passes player data to GameView, and handles the transition to the game end screen.
GameView: A custom View where all the snake game's core logic resides. This includes drawing the game elements (onDraw), handling snake movement (moveSnake), collision detection (checkCollision, checkFoodCollision), food spawning (spawnFood), and processing touch input (onTouchEvent) to change the snake's direction.
GameEndActivity: Displays the final score and game outcome (victory or game over). It provides an option to return to the main menu.
ScoreboardActivity: Responsible for loading and displaying the persistent list of PlayerScore entries using a RecyclerView and ScoreboardAdapter.
PlayerScore: A Kotlin data class acting as the data model for a single player's game record, including methods for JSON serialization.
ScoreboardAdapter: A RecyclerView.Adapter that efficiently binds PlayerScore data to the UI elements of each scoreboard list item.
FileHelper.kt (Implicit): (Though not provided in the snippets, its usage is evident) This class would handle the actual reading from and writing to local storage (likely JSON files) for persisting PlayerScore objects.
Getting Started
To get a local copy up and running, follow these simple steps.

Prerequisites
Android Studio: Latest version recommended (Bumblebee, Chipmunk, Dolphin, Electric Eel, etc.).
Android SDK: Target API Level 30+ (Android 11) is recommended, but adjust build.gradle if targeting a different version.
Installation
Clone the repository:
Bash

git clone https://github.com/your-username/vgp235-final-snakegame.git
cd vgp235-final-snakegame
Open in Android Studio:
Launch Android Studio.
Select "Open an existing Android Studio project" and navigate to the cloned directory.
Build and Run:
Connect an Android device via USB debugging or set up an Android Emulator.
Click the "Run 'app'" button (green triangle icon) in the toolbar.
The application should build and launch on your selected device/emulator.

How to Play
Start the App: Launch the "Android Snake Game" from your device's app drawer.
Main Menu:
Tap "Start Game" to begin a new session.
Tap "Scoreboard" to view past scores.
Player Info: Enter your Name, Age, and Country. Tap "Save" to proceed.
Gameplay:
The snake moves automatically.
To change direction: Tap anywhere on the screen. The snake will turn based on whether your tap was primarily to the left/right or up/down relative to its head.
Eat the colored food items (squares, circles, triangles) to grow and earn points.
Avoid colliding with the walls or the snake's own body.
Game End: Upon collision or victory (reaching 15 segments), the game will transition to the Game End screen, showing your final score and time.
Back to Main Menu: From the Game End or Scoreboard screen, tap "Back to Main Menu" to return to the start.
Future Enhancements
Improved Touch Controls: Implement swipe gestures instead of tap areas for more precise control.
Sound Effects and Music: Add audio cues for eating food, collisions, game over, etc.
Power-Ups: Introduce special food items that grant temporary abilities (e.g., speed boost, invincibility, score multiplier).
Multiple Levels/Themes: Vary board sizes, obstacles, or visual themes.
Online Leaderboards: Integrate with a backend service (e.g., Firebase, Play Games Services) for global leaderboards.
Animations: Smoother snake movement and food consumption animations.
Settings Screen: Allow users to adjust volume, control sensitivity, or change game difficulty.
Contributing
Contributions are welcome! If you have suggestions or want to improve the game, feel free to:

Fork the repository.
Create your feature branch (git checkout -b feature/AmazingFeature).
Commit your changes (git commit -m 'Add some AmazingFeature').
Push to the branch (git push origin feature/AmazingFeature).
Open a Pull Request.
License
Distributed under the MIT License. See LICENSE for more information.

Author
Lukas Kaelin


Project Link: https://github.com/LukasVGP/vgp235_final_snakegame
