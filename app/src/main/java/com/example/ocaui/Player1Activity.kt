package com.example.ocaui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Player1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player1)

        val nameInput = findViewById<EditText>(R.id.playerNameInput)
        val nextButton = findViewById<Button>(R.id.nextButton)

        nextButton.setOnClickListener {
            val intent = Intent(this, Player2Activity::class.java).apply {
                putExtra("PLAYER1_NAME", nameInput.text.toString())
            }
            startActivity(intent)
        }
    }
}
