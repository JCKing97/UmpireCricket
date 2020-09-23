package com.jcking97.umpirecricket

import java.io.Serializable
import java.util.*

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
    private val eventStack: Stack<Event> = Stack<Event>()): Serializable {

    /**
     * Enable the creation of innings
     */
    companion object {

        /**
         * Reload an innings from the last saved file.
         */
        fun fromFile(): Innings {
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
    fun endOver(causedByPreviousEvent: Boolean = false) {
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
            do {
                var event: Event = eventStack.pop()
                when (event.eventType) {
                    EventType.BALL_BOWLED -> undoBallBowled()
                    EventType.EXTRA_BALL -> undoExtraBall()
                    EventType.OVER_BOWLED -> undoEndOver()
                }
            } while (event.causedByPreviousEvent)
        } catch(e: EmptyStackException) {
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

}