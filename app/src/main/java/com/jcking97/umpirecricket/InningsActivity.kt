package com.jcking97.umpirecricket

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
// import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

/**
 * The activity used when running an innings.
 */
class InningsActivity : AppCompatActivity() {

    /**
     * The file writer to write innings data and events to.
     */
    private lateinit var inningsFileWriter: InningsFileWriter

    /**
     * The innings that belongs to this activity.
     */
    private lateinit var innings: Innings

    /**
     * The events of the innings.
     */
    private lateinit var events: Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_innings)

        // Load the innings
        val inningsLoader = intent.getSerializableExtra("inningsLoader") as InningsLoader
        val inningsEvents = inningsLoader.loadInnings()
        innings = inningsEvents.innings
        events = inningsEvents.events

        if (events.isEmpty()) {
            selectBowler(innings)
        }

        // Get the file writer
        inningsFileWriter = intent.getSerializableExtra("inningsFileWriter") as InningsFileWriter

        // Bind buttons to actions
        val ballBowledButton = findViewById<Button>(R.id.ballBowledButton)
        ballBowledButton.setOnClickListener{ inningsAction { ballBowled() } }

        val extraBallButton = findViewById<Button>(R.id.extraBallButton)
        extraBallButton.setOnClickListener{ inningsAction { extraBall() } }

        val endOverButton = findViewById<Button>(R.id.endOverButton)
        endOverButton.setOnClickListener{ inningsAction { endOver() } }

        val undoLastActionButton = findViewById<Button>(R.id.undoButton)
        undoLastActionButton.setOnClickListener{ inningsAction { undoEvent() } }

        updateDisplayText()
    }

    /**
     * Execute an action that takes place in the activity,
     * update the display text and update the file recording the data.
     *
     * @param action An method that represents the action taking place.
     */
    private fun inningsAction(action: () -> Boolean) {
        var triggeredSelectBowler = action()
        updateDisplayText()
        inningsFileWriter.writeEvents(events)
        if (triggeredSelectBowler) {
            selectBowler(innings)
        }
    }

    /**
     * Update the display text with data from the innings.
     */
    private fun updateDisplayText() {
        val ballCountText = findViewById<TextView>(R.id.ballCount)
        val currentOver = innings.getCurrentOver()
        ballCountText.text = "${currentOver.ballsBowled} / ${currentOver.ballLimit}"
        val overCountText = findViewById<TextView>(R.id.oversCountText)
        overCountText.text = "Overs: ${innings.getOversBowled()}"
        if (currentOver.bowler != null) {
            val bowlerNameText = findViewById<TextView>(R.id.bowlerNameText)
            bowlerNameText.text = "Bowler: ${currentOver.bowler!!.name}"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                data.apply {
                    innings = getSerializableExtra("innings") as Innings
                    events = getSerializableExtra("events") as Events
                }
            }
        }
        updateDisplayText()
    }

    /**
     * Allow the user to select the next over's bowler.
     */
    fun selectBowler(innings: Innings) {
        val intent = Intent(this, BowlerActivity::class.java)
        intent.putExtra("innings", innings)
        intent.putExtra("events", events)
        startActivityForResult(intent, 1)
    }

    /**
     * Execute a ball bowled action.
     */
    private fun ballBowled(): Boolean {
        val ballBowledEvent = BallBowledEvent(innings)
        return events.executeEvent(ballBowledEvent)
    }

    /**
     * Execute an extra ball action.
     */
    private fun extraBall(): Boolean {
        val extraBallEvent = ExtraBallEvent(innings)
        return events.executeEvent(extraBallEvent)
    }

    /**
     * Execute an end over action.
     */
    private fun endOver(): Boolean {
        val endOverEvent = EndOverEvent(innings,false)
        return events.executeEvent(endOverEvent)
    }

    /**
     * Undo the last event and any events it was caused by.
     */
    private fun undoEvent(): Boolean {
        return events.undoLastEventAndCausingEvents()
    }
}