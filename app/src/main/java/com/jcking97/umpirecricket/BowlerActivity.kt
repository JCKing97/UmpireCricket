package com.jcking97.umpirecricket

import android.R.attr
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import android.widget.RelativeLayout
import android.R.attr.maxLength

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher


class BowlerActivity : AppCompatActivity() {

    private lateinit var table: TableLayout
    private lateinit var innings: Innings
    private lateinit var events: Events

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bowler)

        innings = intent.getSerializableExtra("innings") as Innings
        events = intent.getSerializableExtra("events") as Events

        table = findViewById<TableLayout>(R.id.bowlerTable)

        initialiseTableRowNames(innings.bowlers)

        val extraBallButton = findViewById<Button>(R.id.newBowlerButton)
        extraBallButton.setOnClickListener{ newBowler() }
    }

    private fun initialiseTableRowNames(bowlers: ArrayList<Bowler>) {
        for (bowler in bowlers) {
            newBowler(bowler)
        }
    }

    private fun createNewBowler(): Bowler {
        val event = NewBowlerEvent(innings)
        events.executeEvent(event)
        return event.bowler
    }

    private fun newBowler(bowler: Bowler? = null) {
        if (innings.bowlers.size <= 11) {
            val newBowler: Bowler
            if (bowler != null) {
                newBowler = bowler
            } else {
                newBowler = createNewBowler()
            }
            val newRow = TableRow(applicationContext)
            newRow.setLayoutParams(TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT))
            val bowlerNameText = EditText(applicationContext)
            bowlerNameText.setFilters(arrayOf<InputFilter>(LengthFilter(30)))
            bowlerNameText.setText(newBowler.name)
            bowlerNameText.setSingleLine()
            bowlerNameText.addTextChangedListener(BowlerNameTextWatcher(newBowler, innings, events))
            val bowlerNameTextLayout = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT)
            bowlerNameTextLayout.span = 80
            bowlerNameText.setLayoutParams(bowlerNameTextLayout)
            val bowlerSelectButton = Button(applicationContext)
            bowlerSelectButton.text = "\u27a4"
            bowlerSelectButton.setOnClickListener { selectBowler(newBowler) }
            newRow.addView(bowlerNameText)
            newRow.addView(bowlerSelectButton)
            table.addView(newRow)
        }
    }

    private fun selectBowler(bowler: Bowler) {
        innings.getCurrentOver().bowler = bowler
        val data = intent.apply {
            putExtra("innings", innings)
            putExtra("events", events)
        }
        setResult(RESULT_OK, data)
        finish()
    }
}