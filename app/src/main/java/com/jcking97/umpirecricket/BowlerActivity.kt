package com.jcking97.umpirecricket

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class BowlerActivity : AppCompatActivity() {

    private var nextOverBowler = 0
    private var lastOverBowler = 0
    private lateinit var bowlerTable: TableLayout
    private lateinit var bowlers: Bowlers

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bowler)

        bowlers = Bowlers()
        bowlerTable = findViewById(R.id.bowlerTable)

        initialiseBowlerTable()

//        initialiseLastBowler(lastOverBowler)
//        attachSelectBowlerListeners(bowlers)
    }

    private fun initialiseBowlerTable() {
        for (bowler in bowlers) {
            val bowlerRow = TableRow(applicationContext)
            val bowlerName = BowlerNameEditText(applicationContext, bowler)
            bowlerName.initialise()
            bowlerRow.addView(bowlerName)
            bowlerTable.addView(bowlerRow)
//            if (bowlers.currentBowler?.equals(index) == false) {
//                initialiseLastBowler(index)
//            }
        }
    }

//    private fun initialiseLastBowler(lastOverBowler: Int) {
//        this.lastOverBowler = lastOverBowler
//        val lastBowlerIntIndex = lastOverBowler + 1
//        // Colour text background
//        val lastBowlerText = findViewById<TextView>(
//            applicationContext.resources.getIdentifier(
//                "bowler${lastBowlerIntIndex}", "id", packageName
//            )
//        )
//        lastBowlerText.setBackgroundColor(resources.getColor(R.color.red))
//        lastBowlerText.setTextColor(resources.getColor(R.color.white))
//        // Disable select button
//        val lastBowlerButton = findViewById<Button>(
//            applicationContext.resources.getIdentifier(
//                "bowler${lastBowlerIntIndex}Button", "id", packageName
//            )
//        )
//        lastBowlerButton.isEnabled = false
//        lastBowlerButton.setBackgroundColor(resources.getColor(R.color.grey))
//    }

//    private fun selectBowler(bowlerIndex: Int) {
//        val oldBowlerText = findViewById<TextView>(
//            applicationContext.resources.getIdentifier(
//                "bowler${selectedBowler+1}", "id", packageName
//            )
//        )
//        oldBowlerText.background = defaultTextBackground
//        oldBowlerText.setTextColor(defaultTextColor)
//        selectedBowler = bowlerIndex
//        val newBowlerText = findViewById<TextView>(
//            applicationContext.resources.getIdentifier(
//                "bowler${selectedBowler+1}", "id", packageName
//            )
//        )
//        newBowlerText.setBackgroundColor(resources.getColor(R.color.green))
//        newBowlerText.setTextColor(resources.getColor(R.color.white))
//    }
//
//    private fun attachSelectBowlerListeners(bowlers: Array<String>) {
//        for (i in bowlers.indices) {
//            val bowlerButton = findViewById<Button>(
//                applicationContext.resources.getIdentifier(
//                    "bowler${i+1}Button", "id", packageName
//                )
//            )
//            bowlerButton.setOnClickListener{ selectBowler(i) }
//        }
//    }
}