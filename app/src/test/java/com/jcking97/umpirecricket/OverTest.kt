package com.jcking97.umpirecricket

import org.junit.Test
import org.junit.Before

import org.junit.Assert.*
import java.lang.Integer.min

/**
 * Tests for the Over class.
 */
class OverTest {

    private lateinit var over: Over;

    @Before
    fun setUp() {
        over = Over()
    }

    @Test
    fun whenBallsBowledThenCurrentBallIncreasesAndBallLimitDoesNot() {
        for (i in 1..3) {
            over.ballBowled()
            assertEquals(i, over.ballsBowled)
            assertEquals(6, over.ballLimit)
        }
    }

    @Test
    fun whenExtraBallThenCurrentBallIncreasesAndBallLimitIncreases() {
        for (i in 1..3) {
            over.extraBall()
            assertEquals(i, over.ballsBowled)
            assertEquals(6+i, over.ballLimit)
        }
    }

    @Test
    fun whenBallBowledReachesLimitThenBallsBowledDoesNotIncreaseAndLimitDoesNotIncrease() {
        for (i in 1..10) {
            over.ballBowled()
            assertEquals(min(i, 6), over.ballsBowled)
            assertEquals(6, over.ballLimit)
        }
    }

    @Test
    fun whenExtraBallsAndBallsBowledThenNewLimitIsRespected() {
        val extraBalls: Int = 3
        for (i in 1..extraBalls) {
            over.extraBall()
            assertEquals(i, over.ballsBowled)
            assertEquals(6+i, over.ballLimit)
        }
        for (i in 1..6) {
            over.ballBowled()
            assertEquals(i+extraBalls, over.ballsBowled)
            assertEquals(6+extraBalls, over.ballLimit)
        }
        for (i in 1..3) {
            over.ballBowled()
            assertEquals(6+extraBalls, over.ballsBowled)
            assertEquals(6+extraBalls, over.ballLimit)
        }
    }

    @Test
    fun whenBallLimitMetOrExceededThenOverEnded() {
        for (i in 1..over.ballLimit) {
            over.ballBowled()
        }
        assertTrue(over.isBallLimitMetOrExceeded())
    }

    @Test
    fun whenBallLimitNotMetOrExceededThenOverNotEnded() {
        for (i in 1..over.ballLimit-1) {
            over.ballBowled()
            assertFalse(over.isBallLimitMetOrExceeded())
        }
    }
}