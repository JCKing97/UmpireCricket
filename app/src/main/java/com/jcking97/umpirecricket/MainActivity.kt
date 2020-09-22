package com.jcking97.umpirecricket

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    var game = Game()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up new and continue game buttons
        val newGameActivity = findViewById<Button>(R.id.newGameButton)
        newGameActivity.setOnClickListener{ newGame() }

        val continueGameActivity = findViewById<Button>(R.id.continueGameButton)
        continueGameActivity.setOnClickListener{ continueGame() }
    }

    private fun newGame() {
        val intent = Intent(this, OverActivity::class.java)
        game = Game.newGame()
        intent.putExtra("game", game)
        startActivity(intent)
    }

    private fun continueGame() {
        val intent = Intent(this, OverActivity::class.java)
        game = Game.fromFile()
        intent.putExtra("game", game)
        startActivity(intent)
    }
}