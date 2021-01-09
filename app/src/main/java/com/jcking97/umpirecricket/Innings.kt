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
 * @property bowlers The bowlers.
 * @property oversBowled The number of overs bowled so far in the innings.
 * @property overs A list of overs bowled in order that they have been bowled.
 * @property eventStack A stack of all the events e.g. balls and overs bowled that have happened
 */
class Innings private constructor(
    var bowlers: Array<String> = Array(11) {"Bowler ${it+1}"},
    private var oversBowled: Int = 0,
    val overs: MutableList<Over> = mutableListOf(Over()),
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
                                EventType.BALL_BOWLED -> innings.ballBowledAndEndOverCheck()
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
     * A ball has been bowled in the current over. Add to the balls bowled and if this has caused
     * the over to end end the over and return True, or else return False.
     *
     * @return Whether the over has ended or not
     */
    fun ballBowledAndEndOverCheck(): Boolean {
        ballBowled()
        return endOverCheck()

    }

    /**
     * Add to the balls bowled and add ball bowled event.
     */
    private fun ballBowled() {
        getCurrentOver().ballBowled()
        eventStack.push(Event(EventType.BALL_BOWLED))
    }

    /**
     * Check if the amount of balls bowled has exceeded the limit for the over. If so end the over,
     * return true if over ended or false if not.
     *
     * @return true if over has ended or false if not.
     */
    private fun endOverCheck(): Boolean {
        val isOverEnded = getCurrentOver().isBallLimitMetOrExceeded()
        if (isOverEnded) {
            endOver(causedByPreviousEvent = true)
        }
        return isOverEnded
    }

    /**
     * Undo the last ball that was bowled.
     */
    private fun undoBallBowled() {
        getCurrentOver().undoBallBowled()
    }

    /**
     * A wide, no-ball or other event has occurred to require an extra ball in the current over.
     * Increase the ball limit and add to the balls bowled.
     *
     * @return Whether the over has ended or not
     */
    fun extraBall(): Boolean {
        getCurrentOver().extraBall()
        eventStack.push(Event(EventType.EXTRA_BALL))
        return false
    }

    /**
     * Undo the last extra ball that was added.
     */
    private fun undoExtraBall() {
        getCurrentOver().undoExtraBall()
    }

    /**
     * End the current over and create a new one.
     *
     * @return Whether the over has ended or not
     */
    fun endOver(): Boolean {
        endOver(causedByPreviousEvent = false)
        return true
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
        if (overs.isNotEmpty()) {
            overs.removeAt(overs.lastIndex)
        }
    }

    /**
     * Undo the last event e.g. ball bowled, over bowled, extra ball.
     *
     * @return Whether the over has ended or not
     */
    fun undoLastAction(): Boolean {
        try {
           undoLastActionAndAllThatCausedTheAction()
        } catch(e: NoSuchElementException) {
            // No action to undo so do nothing
        }
        return false
    }

    /**
     * Take the last action and all prior actions that caused it and
     * do the inverse of their operation.
     *
     * @throws NoSuchElementException if there are no actions to undo.
     */
    @Throws(NoSuchElementException::class)
    private fun undoLastActionAndAllThatCausedTheAction() {
        // Undo one event and any that caused that and prior events
        do {
            // Pop last event and execute inverse
            val event: Event = eventStack.pop()
            doEventInverse(event)
        } while (event.causedByPreviousEvent)
    }

    /**
     * Do the inverse of the given event.
     *
     * @param event The event to do the inverse of.
     */
    private fun doEventInverse(event: Event) {
        when (event.eventType) {
            EventType.BALL_BOWLED -> undoBallBowled()
            EventType.EXTRA_BALL -> undoExtraBall()
            EventType.OVER_BOWLED -> undoEndOver()
        }
    }

    /**
     * @return the number of balls bowled so far this over.
     */
    fun getBallsBowled(): Int {
        return getCurrentOver().ballsBowled
    }

    /**
     * @return the limit of balls for the current over.
     */
    fun getBallLimit(): Int {
        return getCurrentOver().ballLimit
    }

    /**
     * @return the number of overs bowled so far this innings.
     */
    fun getOversBowled(): Int {
        return oversBowled
    }

    /**
     * @return the current over
     */
    private fun getCurrentOver(): Over {
        return overs[oversBowled]
    }

    /**
     * Write the innings to file for data persistence.
     * Write json as a list of events that have happened so far in the game.
     */
    fun writeToInningsFile(context: Context) {
        val json = getInningsEventsAsJson()
        val file = File(context.filesDir, filename)
        writeJsonToFile(json, file)
    }

    /**
     * Get the events in the innings as an ordered JSONArray.
     *
     * @return The JSONArray of events.
     */
    private fun getInningsEventsAsJson(): JSONArray {
        val json = JSONArray()
        eventStack.forEach {
            json.put(it.toJson())
        }
        return json
    }

    /**
     * Write the given JSONArray to the given File.
     *
     * @param json The JSONArray to write to file.
     * @param file The File to write the JSONArray to
     */
    private fun writeJsonToFile(json: JSONArray, file: File) {
        FileWriter(file).use {
            try {
                it.write(json.toString())
            } catch (e: IOException) {
                Log.e("innings.writeToFile", "Failed to write to file", e)
            }
        }
    }

}