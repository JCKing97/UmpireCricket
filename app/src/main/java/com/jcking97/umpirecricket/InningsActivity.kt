package com.jcking97.umpirecricket

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
    private fun inningsAction(action: () -> Unit) {
        action()
        updateDisplayText()
        inningsFileWriter.writeEvents(events)
//        if (overEnded) {
//            selectBowler(innings)
//        }
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
    }

//    /**
//     * Allow the user to select the next over's bowler.
//     */
//    private fun selectBowler(innings: Innings) {
//        val intent = Intent(this, BowlerActivity::class.java)
//        intent.putExtra("bowlers", innings.bowlers)
//        intent.putExtra("lastOverBowler", innings.overs.last().bowlerIndex)
//        intent.putExtra("newOverBowler", 2)
//        startActivity(intent)
//    }

    /**
     * Execute a ball bowled action.
     */
    private fun ballBowled() {
        val ballBowledEvent = BallBowledEvent(innings)
        events.executeEvent(ballBowledEvent)
    }

    /**
     * Execute an extra ball action.
     */
    private fun extraBall() {
        val extraBallEvent = ExtraBallEvent(innings)
        events.executeEvent(extraBallEvent)
    }

    /**
     * Execute an end over action.
     */
    private fun endOver() {
        val endOverEvent = EndOverEvent(innings,false)
        events.executeEvent(endOverEvent)
    }

    /**
     * Undo the last event and any events it was caused by.
     */
    private fun undoEvent() {
        events.undoLastEventAndCausingEvents()
    }
}