package com.jcking97.umpirecricket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class OverActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_over)

        val ballBowledButton = findViewById<Button>(R.id.ballBowledButton)
        ballBowledButton.setOnClickListener{ ballBowled() }

        val extraBallButton = findViewById<Button>(R.id.extraBallButton)
        extraBallButton.setOnClickListener{ extraBall() }

        val endOverButton = findViewById<Button>(R.id.endOverButton)
        endOverButton.setOnClickListener{ endOver() }
    }

    private fun ballBowled() {
        val game = intent.getSerializableExtra("game") as Game
        game.ballBowled()
        updateBallCountAndLimitText()
    }

    private fun extraBall() {
        val game = intent.getSerializableExtra("game") as Game
        game.extraBall()
        updateBallCountAndLimitText()
    }

    private fun updateBallCountAndLimitText() {
        val game = intent.getSerializableExtra("game") as Game
        val ballCountText = findViewById<TextView>(R.id.ballCount)
        ballCountText.text = game.getCurrentBall().toString()
        val ballLimitText = findViewById<TextView>(R.id.ballLimitText)
        ballLimitText.text = game.getBallLimit().toString()
    }

    private fun endOver() {
        val game = intent.getSerializableExtra("game") as Game
        game.endOver()
        val overCountText = findViewById<TextView>(R.id.oversCountText)
        overCountText.text = game.getOverCount().toString()
        updateBallCountAndLimitText()
    }
}