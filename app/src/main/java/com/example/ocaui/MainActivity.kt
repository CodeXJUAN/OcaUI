package com.example.ocaui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    private lateinit var gameLogic: GameLogic
    private lateinit var player1Name: String
    private lateinit var player2Name: String

    private lateinit var player1Card: CardView
    private lateinit var player2Card: CardView
    private lateinit var player1Score: TextView
    private lateinit var player2Score: TextView
    private lateinit var diceImage: ImageView
    private lateinit var rollDiceButton: Button
    private lateinit var messageText: TextView
    private lateinit var gameSection: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Obtener nombres de los jugadores
        player1Name = intent.getStringExtra("PLAYER1_NAME") ?: "Jugador 1"
        player2Name = intent.getStringExtra("PLAYER2_NAME") ?: "Jugador 2"

        initializeViews()
        setupGame()
    }

    private fun initializeViews() {
        player1Card = findViewById(R.id.player1Card)
        player2Card = findViewById(R.id.player2Card)
        player1Score = findViewById(R.id.player1Score)
        player2Score = findViewById(R.id.player2Score)
        diceImage = findViewById(R.id.diceImage)
        rollDiceButton = findViewById(R.id.rollDiceButton)
        messageText = findViewById(R.id.messageText)
        gameSection = findViewById(R.id.gameSection)

        // Configurar botones de reinicio y abandono
        findViewById<Button>(R.id.resetGameButton).setOnClickListener { resetGame() }
        findViewById<Button>(R.id.abandonGameButton).setOnClickListener { abandonGame() }

        // Mostrar nombres de jugadores
        player1Score.text = "$player1Name\nCasilla: 0"
        player2Score.text = "$player2Name\nCasilla: 0"
    }

    private fun setupGame() {
        gameLogic = GameLogic()
        gameLogic.startGame()
        gameSection.visibility = View.VISIBLE
        rollDiceButton.isEnabled = true
        updateTurnIndicator()

        rollDiceButton.setOnClickListener {
            playTurn()
        }
    }

    private fun playTurn() {
        val diceValue = gameLogic.rollDice()
        updateDiceImage(diceValue)

        val gameState = gameLogic.makeMove()
        updatePlayerPositions()
        updateTurnIndicator()

        if (gameState.message.isNotEmpty()) {
            messageText.text = gameState.message
        }

        rollDiceButton.isEnabled = gameState.canRollAgain

        if (gameState.gameEnded) {
            handleGameEnd()
        }
    }

    private fun updateDiceImage(value: Int) {
        val resourceId = resources.getIdentifier(
            "dice_$value",
            "drawable",
            packageName
        )
        diceImage.setImageResource(resourceId)
    }

    private fun updatePlayerPositions() {
        player1Score.text = "$player1Name\nCasilla: ${gameLogic.getPlayer1Position()}"
        player2Score.text = "$player2Name\nCasilla: ${gameLogic.getPlayer2Position()}"
    }

    private fun updateTurnIndicator() {
        val currentPlayer = gameLogic.getCurrentPlayer()
        player1Card.setCardBackgroundColor(
            if (currentPlayer == 1) getColor(R.color.teal_200) else getColor(android.R.color.white)
        )
        player2Card.setCardBackgroundColor(
            if (currentPlayer == 2) getColor(R.color.teal_200) else getColor(android.R.color.white)
        )
    }

    private fun handleGameEnd() {
        val winner = if (gameLogic.getPlayer1Position() >= 63) player1Name else player2Name
        val intent = Intent(this, WinnerActivity::class.java).apply {
            putExtra("WINNER_NAME", winner)
            putExtra("PLAYER1_NAME", player1Name)
            putExtra("PLAYER2_NAME", player2Name)
        }
        startActivity(intent)
    }

    private fun resetGame() {
        gameLogic.resetGame()
        updatePlayerPositions()
        updateTurnIndicator()
        messageText.text = ""
        rollDiceButton.isEnabled = true
    }

    private fun abandonGame() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
