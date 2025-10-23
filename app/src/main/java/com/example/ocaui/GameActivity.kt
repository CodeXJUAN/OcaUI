package com.example.ocaui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    private lateinit var gameLogic: GameLogic
    private lateinit var player1Name: String
    private lateinit var player2Name: String

    private lateinit var player1NameText: TextView
    private lateinit var player2NameText: TextView
    private lateinit var player1ScoreText: TextView
    private lateinit var player2ScoreText: TextView
    private lateinit var player1Indicator: View
    private lateinit var player2Indicator: View
    private lateinit var diceImage: ImageView
    private lateinit var rollDiceButton: Button
    private lateinit var messageText: TextView

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
        player1NameText = findViewById(R.id.player1Name)
        player2NameText = findViewById(R.id.player2Name)
        player1ScoreText = findViewById(R.id.player1Score)
        player2ScoreText = findViewById(R.id.player2Score)
        player1Indicator = findViewById(R.id.player1Indicator)
        player2Indicator = findViewById(R.id.player2Indicator)
        diceImage = findViewById(R.id.diceImage)
        rollDiceButton = findViewById(R.id.rollDiceButton)
        messageText = findViewById(R.id.messageText)

        // Configurar botones de reinicio y abandono
        findViewById<Button>(R.id.resetGameButton).setOnClickListener { resetGame() }
        findViewById<Button>(R.id.abandonGameButton).setOnClickListener { abandonGame() }

        // Mostrar nombres de jugadores
        player1NameText.text = player1Name
        player2NameText.text = player2Name
    }

    private fun setupGame() {
        gameLogic = GameLogic()
        gameLogic.startGame()
        rollDiceButton.isEnabled = true
        messageText.text = "Missatges partida"
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

        if (gameState.message.isNotEmpty()) {
            messageText.text = gameState.message
        } else {
            messageText.text = "Missatges partida"
        }

        if (gameState.gameEnded) {
            handleGameEnd()
        } else {
            if (gameState.canRollAgain) {
                // Si toca otra vez (oca), mantener el botón habilitado
                rollDiceButton.isEnabled = true
            } else {
                // Si no toca otra vez, habilitar el botón para el siguiente jugador
                rollDiceButton.isEnabled = true
            }
            updateTurnIndicator()
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
        player1ScoreText.text = gameLogic.getPlayer1Position().toString()
        player2ScoreText.text = gameLogic.getPlayer2Position().toString()
    }

    private fun updateTurnIndicator() {
        val currentPlayer = gameLogic.getCurrentPlayer()

        // Actualizar indicadores de turno (línea verde debajo del jugador activo)
        if (currentPlayer == 1) {
            player1Indicator.setBackgroundColor(getColor(R.color.teal_700))
            player2Indicator.setBackgroundColor(getColor(android.R.color.transparent))
        } else {
            player1Indicator.setBackgroundColor(getColor(android.R.color.transparent))
            player2Indicator.setBackgroundColor(getColor(R.color.teal_700))
        }
    }

    private fun handleGameEnd() {
        val winner = if (gameLogic.getPlayer1Position() >= 63) player1Name else player2Name
        val intent = Intent(this, WinnerActivity::class.java).apply {
            putExtra("WINNER_NAME", winner)
            putExtra("PLAYER1_NAME", player1Name)
            putExtra("PLAYER2_NAME", player2Name)
        }
        startActivity(intent)
        finish()
    }

    private fun resetGame() {
        gameLogic.resetGame()
        updatePlayerPositions()
        updateTurnIndicator()
        messageText.text = "Missatges partida"
        rollDiceButton.isEnabled = true
        diceImage.setImageResource(R.drawable.dice_1)
    }

    private fun abandonGame() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}