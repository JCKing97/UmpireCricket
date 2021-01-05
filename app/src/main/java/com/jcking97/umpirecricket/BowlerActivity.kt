package com.jcking97.umpirecricket

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class BowlerActivity : AppCompatActivity() {

    var selectedBowler = 0
    private lateinit var defaultTextBackground: Drawable
    private var defaultTextColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bowler)

        val bowlers = intent.getSerializableExtra("bowlers") as Array<String>
        val lastOverBowler = intent.getSerializableExtra("lastOverBowler") as Int
        val newOverBowler = intent.getSerializableExtra("newOverBowler") as Int
        selectedBowler = newOverBowler

        defaultTextBackground = findViewById<TextView>(
            applicationContext.resources.getIdentifier(
                "bowler1", "id", packageName
            )
        ).background

        defaultTextColor = findViewById<TextView>(
            applicationContext.resources.getIdentifier(
                "bowler1", "id", packageName
            )
        ).currentTextColor

        initialiseTableRowNames(bowlers)
        initialiseLastBowler(lastOverBowler)
        selectBowler(selectedBowler)
        attachSelectBowlerListeners(bowlers)
    }

    private fun initialiseTableRowNames(bowlers: Array<String>) {
        for ((index, bowler) in bowlers.withIndex()) {
            val bowlerText = findViewById<TextView>(
                applicationContext.resources.getIdentifier(
                    "bowler${index+1}", "id", packageName
                )
            )
            bowlerText.text = bowler
        }
    }

    private fun initialiseLastBowler(lastOverBowler: Int) {
        val lastBowlerIntIndex = lastOverBowler + 1
        // Colour text background
        val lastBowlerText = findViewById<TextView>(
            applicationContext.resources.getIdentifier(
                "bowler${lastBowlerIntIndex}", "id", packageName
            )
        )
        lastBowlerText.setBackgroundColor(resources.getColor(R.color.red))
        lastBowlerText.setTextColor(resources.getColor(R.color.white))
        // Disable select button
        val lastBowlerButton = findViewById<Button>(
            applicationContext.resources.getIdentifier(
                "bowler${lastBowlerIntIndex}Button", "id", packageName
            )
        )
        lastBowlerButton.isEnabled = false
        lastBowlerButton.setBackgroundColor(resources.getColor(R.color.grey))
    }

    private fun selectBowler(bowlerIndex: Int) {
        val oldBowlerText = findViewById<TextView>(
            applicationContext.resources.getIdentifier(
                "bowler${selectedBowler+1}", "id", packageName
            )
        )
        oldBowlerText.background = defaultTextBackground
        oldBowlerText.setTextColor(defaultTextColor)
        selectedBowler = bowlerIndex
        val newBowlerText = findViewById<TextView>(
            applicationContext.resources.getIdentifier(
                "bowler${selectedBowler+1}", "id", packageName
            )
        )
        newBowlerText.setBackgroundColor(resources.getColor(R.color.green))
        newBowlerText.setTextColor(resources.getColor(R.color.white))
    }

    private fun attachSelectBowlerListeners(bowlers: Array<String>) {
        for (i in bowlers.indices) {
            val bowlerButton = findViewById<Button>(
                applicationContext.resources.getIdentifier(
                    "bowler${i+1}Button", "id", packageName
                )
            )
            bowlerButton.setOnClickListener{ selectBowler(i) }
        }
    }
}