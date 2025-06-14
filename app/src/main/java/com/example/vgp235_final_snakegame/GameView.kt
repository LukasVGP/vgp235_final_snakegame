package com.example.vgp235_final_snakegame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random
import android.util.Log // Added for logging

// Custom View for drawing the Snake game.
class GameView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // --- Game Configuration ---
    private val BOARD_SIZE = 20 // 20x20 grid (coordinates 0-19)
    private val CELL_SIZE_DP = 25f // Base size of each grid cell in DP (will be converted to pixels)
    private var cellSizePx: Float = 0f // Actual cell size in pixels

    // MODIFIED: Increased gameSpeed to make the snake move slower (roughly 1/3 of previous speed, if 200ms was full speed)
    // This is the starting speed.
    var gameSpeed = 600 // Milliseconds between updates (lower is faster).
    private val MIN_GAME_SPEED = 80 // Minimum game speed in milliseconds to prevent it from becoming too fast
    private val SPEED_INCREASE_PERCENTAGE = 0.05f // 5% speed increase per tail piece
    private var snakeLengthForBonus = 5 // Bonus points start after this many tail pieces

    // --- Game State Variables ---
    private var snake: MutableList<Pair<Int, Int>> = mutableListOf() // List of snake segments (x, y)
    private var snakeDirection: Direction = Direction.RIGHT // Initial direction
    private var food: Pair<Int, Int>? = null // Current food position

    var currentScore: Int = 0
    private var gameStartTime: Long = 0
    var isGameRunning: Boolean = false
    private var isGamePaused: Boolean = false

    private var playerName: String = "Guest"
    private var playerAge: Int = 0 // FIXED: Changed varplayerAge to playerAge
    private var playerCountry: String = "Unknown"

    // Callback for when the game ends
    var onGameOverListener: ((Int, Long, Boolean) -> Unit)? = null

    // Enum for snake directions
    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }

    // --- Paint Objects for Drawing ---
    private val paintSnakeHead = Paint().apply { color = Color.BLUE }
    private val paintSnakeHeadTriangle = Paint().apply { color = Color.RED }
    private val paintSnakeTail = Paint().apply { color = Color.parseColor("#006400") } // Dark Green
    private val paintFoodSquare = Paint().apply { color = Color.MAGENTA }
    private val paintFoodCircle = Paint().apply { color = Color.YELLOW }
    private val paintFoodTriangle = Paint().apply { color = Color.parseColor("#FFA500") } // Orange
    private val paintBackground = Paint().apply { color = Color.parseColor("#AADDFF") } // Light Blue
    private val paintBorder = Paint().apply { // Paint for the game board border
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 4f // 4 pixel thick border
    }

    init {
        // Calculate actual cell size in pixels based on screen density
        val density = resources.displayMetrics.density
        cellSizePx = CELL_SIZE_DP * density
        Log.d("GameView", "GameView initialized. Cell size: $cellSizePx px. Board size: $BOARD_SIZE x $BOARD_SIZE (0 to ${BOARD_SIZE - 1})")
        startGame() // Initialize game state on creation
    }

    // Sets player details, typically called from GameActivity.
    fun setPlayerDetails(name: String, age: Int, country: String) {
        this.playerName = name
        this.playerAge = age // FIXED: Assigned to correct property
        this.playerCountry = country
    }

    // Resets the game to its initial state and positions the snake.
    fun startGame() {
        snake.clear()
        // ADJUSTED START POSITION:
        // Start further from the middle to give more room, especially for downward turns.
        // For BOARD_SIZE=20 (range 0-19), (5, 5) is a good starting point to give plenty of space.
        val startX = BOARD_SIZE / 4 // e.g., 20/4 = 5
        val startY = BOARD_SIZE / 4 // e.g., 20/4 = 5
        snake.add(Pair(startX, startY))
        snakeDirection = Direction.RIGHT // Snake still starts moving right
        currentScore = 0
        gameStartTime = System.currentTimeMillis()
        isGameRunning = true
        isGamePaused = false
        // Reset game speed to initial value when starting a new game
        gameSpeed = 600 // Ensure speed resets on new game
        spawnFood() // Initial food spawn
        invalidate() // Redraw the view with the new snake position
        Log.d("GameView", "startGame() called. Snake initialized at: ${snake.first()}, Direction: $snakeDirection. Board range: 0 to ${BOARD_SIZE - 1}. Initial gameSpeed: $gameSpeed ms")
    }

    // Pauses the game.
    fun pauseGame() {
        isGamePaused = true
        Log.d("GameView", "Game paused.")
    }

    // Resumes the game.
    fun resumeGame() {
        isGamePaused = false
        Log.d("GameView", "Game resumed.")
    }

    // Main game update logic, called repeatedly by GameActivity's handler.
    fun update() {
        if (!isGameRunning || isGamePaused) return

        val oldHead = snake.first() // Log current head before move
        moveSnake()
        val newHead = snake.first() // Log new head after move
        Log.d("GameView", "Update Cycle: Old Head: $oldHead, New Head: $newHead, Direction: $snakeDirection")

        checkCollision()
        checkFoodCollision()
        invalidate() // Request a redraw
    }

    // Moves the snake based on its current direction.
    private fun moveSnake() {
        val head = snake.first()
        val newHead = when (snakeDirection) {
            Direction.UP -> Pair(head.first, head.second - 1)
            Direction.DOWN -> Pair(head.first, head.second + 1)
            Direction.LEFT -> Pair(head.first - 1, head.second)
            Direction.RIGHT -> Pair(head.first + 1, head.second)
        }
        snake.add(0, newHead) // Add new head
        Log.v("GameView", "moveSnake: Calculated newHead: $newHead. Current snake size: ${snake.size}")

        // If no food eaten, remove tail to simulate movement
        if (newHead != food) {
            snake.removeAt(snake.size - 1)
        }
    }

    // Checks for collisions with walls or self.
    private fun checkCollision() {
        val head = snake.first()
        Log.d("GameView", "checkCollision: Checking Head at (${head.first}, ${head.second}) with snake size ${snake.size}")

        // Wall collision
        if (head.first < 0 || head.first >= BOARD_SIZE ||
            head.second < 0 || head.second >= BOARD_SIZE) {
            Log.e("GameView", "GAME OVER: Wall Collision! Head at (${head.first}, ${head.second}). Board size: $BOARD_SIZE (Valid range 0 to ${BOARD_SIZE - 1}).")
            endGame(false)
            return
        }

        // Self-collision (check if head touches any part of the tail)
        // This loop only runs if snake.size > 1
        for (i in 1 until snake.size) {
            if (head == snake[i]) {
                Log.e("GameView", "GAME OVER: Self Collision! Head at (${head.first}, ${head.second}) collided with tail at (${snake[i].first}, ${snake[i].second}).")
                endGame(false)
                return
            }
        }
    }

    // Checks if the snake head has eaten food.
    private fun checkFoodCollision() {
        val head = snake.first()
        // Collision is still exact: head must occupy the same cell as the food.
        if (head == food) {
            currentScore += 50 // Each fruit gives 50 points
            Log.d("GameView", "Food eaten! Score: $currentScore, Snake size before growth: ${snake.size}")

            // NEW: Increase speed by 5% for every tail piece gained
            // Speed increases by decreasing gameSpeed duration
            gameSpeed = (gameSpeed * (1 - SPEED_INCREASE_PERCENTAGE)).toInt().coerceAtLeast(MIN_GAME_SPEED)
            Log.d("GameView", "Speed increased! New gameSpeed: $gameSpeed ms")
            // The GameActivity's Handler will pick up the new gameSpeed value on its next postDelayed call.

            if (snake.size >= 15) { // Check for victory condition (15 tail pieces)
                endGame(true) // Game over, victory!
            } else {
                spawnFood() // Spawn new food
                // Snake grows automatically because we didn't remove the tail in moveSnake()
            }
        }
    }

    // Spawns food at a random unoccupied position.
    // MODIFIED: Prioritizes spawning food on the snake's current row (Y-coordinate).
    private fun spawnFood() {
        val availablePositions = mutableListOf<Pair<Int, Int>>()
        for (x in 0 until BOARD_SIZE) {
            for (y in 0 until BOARD_SIZE) {
                val position = Pair(x, y)
                if (position !in snake) { // Ensure food doesn't spawn on snake's body
                    availablePositions.add(position)
                }
            }
        }

        val snakeHeadY = snake.first().second // Get the snake's current Y-coordinate
        val potentialPositionsOnSameRow = availablePositions.filter { it.second == snakeHeadY }

        if (potentialPositionsOnSameRow.isNotEmpty()) {
            food = potentialPositionsOnSameRow[Random.nextInt(potentialPositionsOnSameRow.size)]
            Log.d("GameView", "Food spawned on snake's current row ($snakeHeadY) at: $food")
        } else if (availablePositions.isNotEmpty()) {
            // Fallback: If no space on the current row, spawn randomly anywhere else
            food = availablePositions[Random.nextInt(availablePositions.size)]
            Log.w("GameView", "Current row is full or has no available spots, food spawned randomly at: $food")
        } else {
            // If no space anywhere, implies board is full, trigger victory.
            Log.w("GameView", "No space for food left on board. Triggering victory.")
            endGame(true)
        }
    }

    // Ends the game and triggers the onGameOverListener.
    private fun endGame(isVictory: Boolean) {
        if (!isGameRunning) return // Prevent multiple calls if already ended
        val finalGameTime = (System.currentTimeMillis() - gameStartTime) / 1000 // In seconds
        var finalScore = currentScore

        if (isVictory) {
            finalScore += 500 // Add bonus points for victory
        }

        // Add time-based bonus points after 5 tail pieces
        if (snake.size - 1 >= snakeLengthForBonus) {
            finalScore += (finalGameTime * 10).toInt() // 10 points per second
        }
        Log.w("GameView", "endGame() called. Victory: $isVictory. Final Score: $finalScore, Final Time: ${finalGameTime}s")
        isGameRunning = false // Set game to not running before listener to avoid re-triggering loop
        onGameOverListener?.invoke(finalScore, finalGameTime, isVictory)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw background
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)

        // Calculate grid cell dimensions dynamically based on view size
        val cellWidth = width.toFloat() / BOARD_SIZE
        val cellHeight = height.toFloat() / BOARD_SIZE
        cellSizePx = cellWidth.coerceAtMost(cellHeight) // Use the smaller dimension for square cells

        // Draw snake
        for ((index, segment) in snake.withIndex()) {
            val left = segment.first * cellSizePx
            val top = segment.second * cellSizePx
            val right = left + cellSizePx
            val bottom = top + cellSizePx

            if (index == 0) { // Snake head
                canvas.drawRect(left, top, right, bottom, paintSnakeHead)

                // Draw red triangle for head direction
                val headPath = Path()
                when (snakeDirection) {
                    Direction.UP -> {
                        headPath.moveTo(left + cellSizePx / 2, top)
                        headPath.lineTo(left, bottom)
                        headPath.lineTo(right, bottom)
                    }
                    Direction.DOWN -> {
                        headPath.moveTo(left + cellSizePx / 2, bottom)
                        headPath.lineTo(left, top)
                        headPath.lineTo(right, top)
                    }
                    Direction.LEFT -> {
                        headPath.moveTo(left, top + cellSizePx / 2)
                        headPath.lineTo(right, top)
                        headPath.lineTo(right, bottom)
                    }
                    Direction.RIGHT -> {
                        headPath.moveTo(right, top + cellSizePx / 2)
                        headPath.lineTo(left, top)
                        headPath.lineTo(left, bottom)
                    }
                }
                headPath.close()
                canvas.drawPath(headPath, paintSnakeHeadTriangle)
            } else { // Snake tail
                canvas.drawRect(left, top, right, bottom, paintSnakeTail)
            }
        }

        // Draw food
        food?.let {
            val left = it.first * cellSizePx
            val top = it.second * cellSizePx
            val right = left + cellSizePx
            val bottom = top + cellSizePx

            when (Random.nextInt(3)) { // Randomly choose fruit shape
                0 -> canvas.drawRect(left, top, right, bottom, paintFoodSquare) // Square
                1 -> canvas.drawCircle(left + cellSizePx / 2, top + cellSizePx / 2, cellSizePx / 2, paintFoodCircle) // Circle
                2 -> { // Triangle
                    val path = Path()
                    path.moveTo(left + cellSizePx / 2, top)
                    path.lineTo(left, bottom)
                    path.lineTo(right, bottom)
                    path.close()
                    canvas.drawPath(path, paintFoodTriangle)
                }
            }
        }

        // Draw a border around the game board to clearly show boundaries
        val boardLeft = 0f
        val boardTop = 0f
        val boardRight = BOARD_SIZE * cellSizePx
        val boardBottom = BOARD_SIZE * cellSizePx
        canvas.drawRect(boardLeft, boardTop, boardRight, boardBottom, paintBorder)
    }

    // Handles touch input for changing snake direction.
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val touchX = event.x
            val touchY = event.y

            // Get current head position in pixels
            val headX = snake.first().first * cellSizePx + cellSizePx / 2
            val headY = snake.first().second * cellSizePx + cellSizePx / 2

            // Calculate differences
            val dx = touchX - headX
            val dy = touchY - headY

            // Determine new direction based on touch relative to snake head
            // Prevent 180-degree turns
            if (Math.abs(dx) > Math.abs(dy)) { // Horizontal movement
                if (dx > 0 && snakeDirection != Direction.LEFT) {
                    snakeDirection = Direction.RIGHT
                    Log.d("GameView", "Changed direction to RIGHT from touch: ($touchX, $touchY) relative to head ($headX, $headY).")
                } else if (dx < 0 && snakeDirection != Direction.RIGHT) {
                    snakeDirection = Direction.LEFT
                    Log.d("GameView", "Changed direction to LEFT from touch: ($touchX, $touchY) relative to head ($headX, $headY).")
                }
            } else { // Vertical movement
                if (dy > 0 && snakeDirection != Direction.UP) {
                    snakeDirection = Direction.DOWN
                    Log.d("GameView", "Changed direction to DOWN from touch: ($touchX, $touchY) relative to head ($headX, $headY).")
                } else if (dy < 0 && snakeDirection != Direction.DOWN) {
                    snakeDirection = Direction.UP
                    Log.d("GameView", "Changed direction to UP from touch: ($touchX, $touchY) relative to head ($headX, $headY).")
                }
            }
            return true
        }
        return super.onTouchEvent(event)
    }
}
