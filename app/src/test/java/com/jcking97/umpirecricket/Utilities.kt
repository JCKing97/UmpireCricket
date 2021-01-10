package com.jcking97.umpirecricket

import org.junit.Assert

object Utilities {

    fun assertBallsLimitOver(innings: Innings, ballsBowled: Int, ballLimit: Int, oversBowled: Int) {
        Assert.assertEquals(ballsBowled, innings.getCurrentOver().ballsBowled)
        Assert.assertEquals(ballLimit, innings.getCurrentOver().ballLimit)
        Assert.assertEquals(oversBowled, innings.getOversBowled())
    }

}