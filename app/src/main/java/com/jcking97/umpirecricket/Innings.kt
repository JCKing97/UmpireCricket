package com.jcking97.umpirecricket

import java.io.Serializable

/**
 * An innings where one team bats and one team bowls.
 * In our case this consists of a number of overs.
 *
 * @property oversBowled The number of overs bowled so far in the innings.
 * @property overs A list of overs bowled in order that they have been bowled.
 */
class Innings private constructor(
    private var oversBowled: Int = 0,
    private val overs: MutableList<Over> = mutableListOf(Over())): Serializable {

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
        if (overs[oversBowled].ballsBowled >= overs[oversBowled].ballLimit) {
            endOver()
        }
    }

    /**
     * A wide, no-ball or other event has occurred to require an extra ball in the current over.
     * Increase the ball limit and add to the balls bowled.
     */
    fun extraBall() {
        overs[oversBowled].extraBall()
    }

    /**
     * End the current over and create a new one.
     */
    fun endOver() {
        oversBowled++
        overs.add(Over())
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