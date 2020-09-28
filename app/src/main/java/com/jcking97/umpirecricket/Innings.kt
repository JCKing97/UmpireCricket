package com.jcking97.umpirecricket

import android.content.Context
import android.util.Log
import org.json.JSONArray
import java.io.*
import java.util.*
import kotlin.NoSuchElementException

/**
 * An innings where one team bats and one team bowls.
 * In our case this consists of a number of overs.
 *
 * @property oversBowled The number of overs bowled so far in the innings.
 * @property overs A list of overs bowled in order that they have been bowled.
 * @property eventStack A stack of all the events e.g. balls and overs bowled that have happened
 */
class Innings private constructor(
    private var oversBowled: Int = 0,
    private val overs: MutableList<Over> = mutableListOf(Over()),
    private val eventStack: LinkedList<Event> = LinkedList<Event>()): Serializable {

    /**
     * Enable the creation of innings
     */
    companion object {

        private const val filename: String = "innings.json"

        /**
         * Reload an innings from the last saved file.
         */
        fun fromFile(context: Context): Innings {
            val file = File(context.filesDir, filename)
            if (file.exists()) {
                FileReader(file).use {
                    // Retrieve JSON list of events
                    val jsonArray = JSONArray(it.readText())
                    val innings = Innings()
                    // Loop through events (stack built in reverse, so read in reverse)
                    for (i in jsonArray.length() - 1 downTo 0) {
                        val event = Event.fromJson(jsonArray.getJSONObject(i))
                        // Recreate event
                        if (!event.causedByPreviousEvent) {
                            when (event.eventType) {
                                EventType.BALL_BOWLED -> innings.ballBowled()
                                EventType.EXTRA_BALL -> innings.extraBall()
                                EventType.OVER_BOWLED -> innings.endOver()
                            }
                        }
                    }
                    return innings
                }
            }
            return Innings()
        }

        /**
         * Generate a new innings with no overs.
         */
        fun newInnings(): Innings {
            return Innings()
        }

    }

    /**
     * A ball has been bowled in the current over. Add to the balls bowled and if this
     * equals or exceeds the ball limit end the over.
     */
    fun ballBowled() {
        overs[oversBowled].ballBowled()
        eventStack.push(Event(EventType.BALL_BOWLED))
        if (overs[oversBowled].ballsBowled >= overs[oversBowled].ballLimit) {
            endOver(causedByPreviousEvent = true)
        }
    }

    /**
     * Undo the last ball that was bowled.
     */
    private fun undoBallBowled() {
        overs[oversBowled].undoBallBowled()
    }

    /**
     * A wide, no-ball or other event has occurred to require an extra ball in the current over.
     * Increase the ball limit and add to the balls bowled.
     */
    fun extraBall() {
        overs[oversBowled].extraBall()
        eventStack.push(Event(EventType.EXTRA_BALL))
    }

    /**
     * Undo the last extra ball that was added.
     */
    private fun undoExtraBall() {
        overs[oversBowled].undoExtraBall()
    }

    /**
     * End the current over and create a new one.
     */
    fun endOver() {
        endOver(causedByPreviousEvent = false)
    }

    /**
     * End the current over
     */
    private fun endOver(causedByPreviousEvent: Boolean = false) {
        oversBowled++
        overs.add(Over())
        eventStack.push(Event(EventType.OVER_BOWLED, causedByPreviousEvent))
    }

    /**
     * Undo the last end over action.
     */
    private fun undoEndOver() {
        oversBowled--
        overs.removeLastOrNull()
    }

    /**
     * Undo the last event e.g. ball bowled, over bowled, extra ball.
     */
    fun undoLastAction() {
        try {
            // Undo one event and any that caused that and prior events
            do {
                // Pop last event and execute inverse
                val event: Event = eventStack.pop()
                when (event.eventType) {
                    EventType.BALL_BOWLED -> undoBallBowled()
                    EventType.EXTRA_BALL -> undoExtraBall()
                    EventType.OVER_BOWLED -> undoEndOver()
                }
            } while (event.causedByPreviousEvent)
        } catch(e: NoSuchElementException) {
            // No action to undo so do nothing
        }
    }

    /**
     * @return the number of balls bowled so far this over.
     */
    fun getBallsBowled(): Int {
        return overs[oversBowled].ballsBowled
    }

    /**
     * @return the limit of balls for the current over.
     */
    fun getBallLimit(): Int {
        return overs[oversBowled].ballLimit
    }

    /**
     * @return the number of overs bowled so far this innings.
     */
    fun getOversBowled(): Int {
        return oversBowled
    }

    /**
     * Write the innings to file for data persistence.
     * Write json as a list of events that have happened so far in the game.
     */
    fun writeToFile(context: Context) {
        // Generate json events
        val jsonArray = JSONArray()
        eventStack.forEach {
            jsonArray.put(it.toJson())
        }
        // Write json to file
        val file = File(context.filesDir, filename)
        FileWriter(file).use {
            try {
                it.write(jsonArray.toString())
            } catch (e: IOException) {
                Log.e("innings.writeToFile", "Failed to write to file", e)
            }
        }
    }

}