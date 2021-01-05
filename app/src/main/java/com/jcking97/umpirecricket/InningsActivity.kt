package com.jcking97.umpirecricket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class InningsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_innings)

        val innings = intent.getSerializableExtra("innings") as Innings

        val ballBowledButton = findViewById<Button>(R.id.ballBowledButton)
        ballBowledButton.setOnClickListener{ inningsAction(innings) { innings.ballBowled() } }

        val extraBallButton = findViewById<Button>(R.id.extraBallButton)
        extraBallButton.setOnClickListener{ inningsAction(innings) { innings.extraBall() } }

        val endOverButton = findViewById<Button>(R.id.endOverButton)
        endOverButton.setOnClickListener{ inningsAction(innings) { innings.endOver() } }

        val undoLastActionButton = findViewById<Button>(R.id.undoButton)
        undoLastActionButton.setOnClickListener{ inningsAction(innings) { innings.undoLastAction() } }

        updateDisplayText()
    }

    private fun inningsAction(innings: Innings, action: () -> Unit ) {
        action()
        updateDisplayText()
        innings.writeToFile(applicationContext)
    }

    private fun updateDisplayText() {
        val innings = intent.getSerializableExtra("innings") as Innings
        val ballCountText = findViewById<TextView>(R.id.ballCount)
        ballCountText.text = "${innings.getBallsBowled()} / ${innings.getBallLimit()}"
        val overCountText = findViewById<TextView>(R.id.oversCountText)
        overCountText.text = "Overs: ${innings.getOversBowled()}"
    }
}