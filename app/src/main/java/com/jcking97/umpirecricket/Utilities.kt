package com.jcking97.umpirecricket

import java.lang.AssertionError

object Utilities {

    fun assertBallsLimitOver(innings: Innings, expectedBallsBowled: Int, expectedBallLimit: Int, expectedOversBowled: Int) {
        var assertFailed = false
        var message = "Assertion failed:\n"
        val ballsBowled = innings.getCurrentOver().ballsBowled
        if (ballsBowled != expectedBallsBowled) {
            message += "Expected balls bowled: ${expectedBallsBowled}. Actual: ${ballsBowled}.\n"
            assertFailed = true
        }
        val ballLimit = innings.getCurrentOver().ballLimit
        if (ballLimit != expectedBallLimit) {
            message += "Expected ball limit: ${expectedBallLimit}. Actual: ${ballLimit}.\n"
            assertFailed = true
        }
        val oversBowled = innings.getOversBowled()
        if (oversBowled != expectedOversBowled) {
            message += "Expected overs bowled: ${expectedOversBowled}. Actual: ${oversBowled}.\n"
            assertFailed = true
        }
        if (assertFailed) {
            throw AssertionError(message)
        }
    }

}