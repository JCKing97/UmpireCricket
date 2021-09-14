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


class BowlerActivity : AppCompatActivity() {

    private lateinit var table: TableLayout
    private lateinit var bowlers: ArrayList<String>
    private var lastOverBowler: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bowler)

        bowlers = intent.getSerializableExtra("bowlers") as ArrayList<String>
        lastOverBowler = intent.getSerializableExtra("lastOverBowler") as Int

        table = findViewById<TableLayout>(R.id.bowlerTable)

        initialiseTableRowNames(bowlers)

        val extraBallButton = findViewById<Button>(R.id.newBowlerButton)
        extraBallButton.setOnClickListener{ newBowler() }
    }

    private fun initialiseTableRowNames(bowlers: ArrayList<String>) {
        for (bowler in bowlers) {
            newBowler(bowler)
        }
    }

    private fun newBowler(bowler: String? = null) {
        if (bowlers.size <= 11) {
            var bowlerName = "Bowler ${table.childCount}"
            if (bowler != null) {
                bowlerName = bowler
            }
            val newRow = TableRow(applicationContext)
            newRow.setLayoutParams(TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT))
            val bowlerNameText = EditText(applicationContext)
            bowlerNameText.setFilters(arrayOf<InputFilter>(LengthFilter(30)))
            bowlerNameText.setText(bowlerName)
            bowlerNameText.setSingleLine()
            val bowlerNameTextLayout = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT)
            bowlerNameTextLayout.span = 80
            bowlerNameText.setLayoutParams(bowlerNameTextLayout)
            val bowlerSelectButton = Button(applicationContext)
            bowlerSelectButton.text = "\u27a4"
            if (table.childCount == lastOverBowler) {
                bowlerSelectButton.setAlpha(.5f);
                bowlerSelectButton.setClickable(false);
            } else {
                bowlerSelectButton.setOnClickListener { selectBowler(table.childCount) }
            }
            newRow.addView(bowlerNameText)
            newRow.addView(bowlerSelectButton)
            table.addView(newRow)
            bowlers.add(bowlerName)
        }
    }

    private fun selectBowler(bowlerIndex: Int) {
        val data = intent.apply {
            putExtra("SELECTED_BOWLER", bowlerIndex)
        }
        setResult(RESULT_OK, data)
        finish()
    }
}