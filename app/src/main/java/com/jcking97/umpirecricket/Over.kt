package com.jcking97.umpirecricket

import java.io.Serializable

/**
 * A singular over. Generally made up of six balls unless wides or no balls are bowled.
 *
 * @property bowlerIndex The index of the bowler that bowled this over.
 * @property ballsBowled The number of balls bowled so far in the over.
 * @property ballLimit The maximum number of balls to be bowled in the over unless an event occurs
 *  to increase that number during the over.
 * @constructor Creates an over and sets the currentBall and ballLimit
 */
class Over(
    var bowlerIndex: Int = 0,
    var ballsBowled: Int = 0,
    var ballLimit: Int = 6): Serializable {

    /**
     * A ball has been bowled increase the count.
     */
    fun ballBowled() {
        if (!isBallLimitMetOrExceeded()) {
            ballsBowled++
        }
    }

    /**
     * Undo a ball being bowled.
     */
    fun undoBallBowled() {
        ballsBowled--
    }

    /**
     * A no-ball, wide or other event has occurred that causes an extra
     * ball to be bowled in the over. As such increase the ball limit for this over.
     */
    fun extraBall() {
        ballLimit++
        ballBowled()
    }

    /**
     * Undo an extra ball.
     */
    fun undoExtraBall() {
        ballLimit--
        undoBallBowled()
    }

    /**
     * Has the ball limit been met or exceeded.
     *
     * @return true if ball limit met or exceeded, false if not.
     */
    fun isBallLimitMetOrExceeded(): Boolean {
        return ballsBowled >= ballLimit
    }
}