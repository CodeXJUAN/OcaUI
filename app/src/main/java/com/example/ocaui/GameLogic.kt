package com.example.ocaui

class GameLogic {
    private var currentPlayer = 1
    private var player1Position = 0
    private var player2Position = 0
    private val ocaCells = setOf(5, 9, 14, 18, 23, 27, 32, 36, 41, 45, 50, 54, 59)
    private val deathCell = 58
    private var isGameActive = false
    private var lastDiceValue = 1

    fun getCurrentPlayer() = currentPlayer
    fun getPlayer1Position() = player1Position
    fun getPlayer2Position() = player2Position
    fun getLastDiceValue() = lastDiceValue
    fun isGameActive() = isGameActive

    fun startGame() {
        isGameActive = true
        resetGame()
    }

    fun rollDice(): Int {
        lastDiceValue = (1..6).random()
        return lastDiceValue
    }

    fun makeMove(): GameState {
        val result = movePlayer(lastDiceValue)
        return when (result) {
            GameResult.WIN -> GameState(
                message = "¡Ha ganado!",
                canRollAgain = false,
                gameEnded = true
            )
            GameResult.DEATH -> GameState(
                message = "¡Casilla de la muerte! Vuelves al inicio.",
                canRollAgain = false
            )
            GameResult.OCA -> GameState(
                message = "¡De oca a oca y tiro porque me toca!",
                canRollAgain = true
            )
            GameResult.NORMAL -> GameState(
                message = "",
                canRollAgain = false
            )
        }
    }

    private fun movePlayer(diceValue: Int): GameResult {
        val currentPosition = if (currentPlayer == 1) player1Position else player2Position
        var newPosition = currentPosition + diceValue

        if (newPosition > 63) {
            newPosition = 63 - (newPosition - 63)
        }

        if (currentPlayer == 1) {
            player1Position = newPosition
        } else {
            player2Position = newPosition
        }

        return checkSpecialCells(newPosition)
    }

    private fun checkSpecialCells(position: Int): GameResult {
        return when {
            position == 63 -> GameResult.WIN
            position == deathCell -> {
                if (currentPlayer == 1) player1Position = 0 else player2Position = 0
                switchTurn()
                GameResult.DEATH
            }
            position in ocaCells -> GameResult.OCA
            else -> {
                switchTurn()
                GameResult.NORMAL
            }
        }
    }

    private fun switchTurn() {
        currentPlayer = if (currentPlayer == 1) 2 else 1
    }

    fun resetGame() {
        currentPlayer = 1
        player1Position = 0
        player2Position = 0
        isGameActive = true
    }
}

enum class GameResult {
    NORMAL,
    OCA,
    DEATH,
    WIN
}

data class GameState(
    val message: String,
    val canRollAgain: Boolean,
    val gameEnded: Boolean = false
)
