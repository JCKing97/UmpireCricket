package com.jcking97.umpirecricket

import org.junit.Assert
import java.lang.AssertionError

object Utilities {

    fun assertBallsLimitOver(innings: Innings, ballsBowled: Int, ballLimit: Int, oversBowled: Int) {
        var assertFailed = false
        var message = "Assertion failed:\n"
        val expectedBallsBowled = innings.getCurrentOver().ballsBowled
        if (ballsBowled != expectedBallsBowled) {
            message += "Expected balls bowled: ${expectedBallsBowled}. Actual: ${ballsBowled}.\n"
            assertFailed = true
        }
        val expectedBallLimit = innings.getCurrentOver().ballLimit
        if (ballLimit != expectedBallLimit) {
            message += "Expected ball limit: ${expectedBallLimit}. Actual: ${ballLimit}.\n"
            assertFailed = true
        }
        val expectedOversBowled = innings.getOversBowled()
        if (oversBowled != expectedOversBowled) {
            message += "Expected overs bowled: ${expectedOversBowled}. Actual: ${oversBowled}.\n"
            assertFailed = true
        }
        if (assertFailed) {
            throw AssertionError(message)
        }
    }

}