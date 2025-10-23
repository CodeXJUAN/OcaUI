package com.example.ocaui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WinnerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_winner)

        val winnerName = intent.getStringExtra("WINNER_NAME") ?: ""
        val player1Name = intent.getStringExtra("PLAYER1_NAME") ?: ""
        val player2Name = intent.getStringExtra("PLAYER2_NAME") ?: ""

        findViewById<TextView>(R.id.winnerText).text = "¡Felicidades $winnerName!"

        // Botón de reiniciar vuelve al juego con los mismos jugadores
        findViewById<Button>(R.id.restartButton).setOnClickListener {
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra("PLAYER1_NAME", player1Name)
                putExtra("PLAYER2_NAME", player2Name)
            }
            startActivity(intent)
        }

        // Botón de abandonar vuelve a la pantalla de bienvenida
        findViewById<Button>(R.id.quitButton).setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finishAffinity()
        }
    }
}
