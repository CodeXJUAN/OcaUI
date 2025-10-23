package com.example.ocaui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Player2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player2)

        val player1Name = intent.getStringExtra("PLAYER1_NAME") ?: ""
        val nameInput = findViewById<EditText>(R.id.playerNameInput)
        val nextButton = findViewById<Button>(R.id.nextButton)

        nextButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra("PLAYER1_NAME", player1Name)
                putExtra("PLAYER2_NAME", nameInput.text.toString())
            }
            startActivity(intent)
        }
    }
}
