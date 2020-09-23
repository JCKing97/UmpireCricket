package com.jcking97.umpirecricket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class InningsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_innings)

        val ballBowledButton = findViewById<Button>(R.id.ballBowledButton)
        ballBowledButton.setOnClickListener{ ballBowled() }

        val extraBallButton = findViewById<Button>(R.id.extraBallButton)
        extraBallButton.setOnClickListener{ extraBall() }

        val endOverButton = findViewById<Button>(R.id.endOverButton)
        endOverButton.setOnClickListener{ endOver() }
    }

    private fun ballBowled() {
        val innings = intent.getSerializableExtra("innings") as Innings
        innings.ballBowled()
        updateDisplayText()
    }

    private fun extraBall() {
        val innings = intent.getSerializableExtra("innings") as Innings
        innings.extraBall()
        updateDisplayText()
    }

    private fun updateDisplayText() {
        val innings = intent.getSerializableExtra("innings") as Innings
        val ballCountText = findViewById<TextView>(R.id.ballCount)
        ballCountText.text = "${innings.getBallsBowled()} / ${innings.getBallLimit()}"
        val overCountText = findViewById<TextView>(R.id.oversCountText)
        overCountText.text = "Overs: ${innings.getOversBowled()}"
    }

    private fun endOver() {
        val innings = intent.getSerializableExtra("innings") as Innings
        innings.endOver()
        updateDisplayText()
    }
}