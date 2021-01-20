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
        for (i in 1 until over.ballLimit) {
            over.ballBowled()
            assertFalse(over.isBallLimitMetOrExceeded())
        }
    }

    @Test
    fun givenNoBallBowledWhenUndoBallBowledThenBallNotUndo() {
        for (i in 1..3) {
            over.undoBallBowled()
            assertEquals(0, over.ballsBowled)
        }
    }

    @Test
    fun givenNoExtraBallBowledWhenUndoBallBowledThenBallNotUndone() {
        for (i in 1..3) {
            over.undoExtraBall()
            assertEquals(0, over.ballsBowled)
            assertEquals(6, over.ballLimit)
        }
    }

    @Test
    fun givenBallBowledWhenUndoExtraBallThenBallNotUndone() {
        val balls = 3
        for (ball in 1..balls) {
            over.ballBowled()
        }
        for (ball in balls downTo 1) {
            over.undoExtraBall()
            assertEquals(balls, over.ballsBowled)
            assertEquals(6, over.ballLimit)
        }
    }

    @Test
    fun givenExtraBallsWhenUndoBallBowledThenBallUndoneAndNotExtraUndone() {
        val balls = 3
        for (ball in 1..balls) {
            over.extraBall()
        }
        for (ball in balls-1 downTo 0) {
            over.undoBallBowled()
            assertEquals(ball, over.ballsBowled)
            assertEquals(6+balls, over.ballLimit)
        }
    }

    @Test
    fun givenBallsBowledWhenUndoBallsBowledThenBallsUndone() {
        val balls = 3
        for (ball in 1..balls) {
            over.ballBowled()
        }
        for (ball in balls-1 downTo 0) {
            over.undoBallBowled()
            assertEquals(ball, over.ballsBowled)
            assertEquals(6, over.ballLimit)
        }
        over.undoBallBowled()
        assertEquals(0, over.ballsBowled)
        assertEquals(6, over.ballLimit)
    }

    @Test
    fun givenExtraBallsWhenUndoExtraBallsThenExtraBallsUndone() {
        val balls = 3
        for (ball in 1..balls) {
            over.extraBall()
        }
        for (ball in balls-1 downTo 0) {
            over.undoExtraBall()
            assertEquals(ball, over.ballsBowled)
            assertEquals(6+ball, over.ballLimit)
        }
        over.undoBallBowled()
        assertEquals(0, over.ballsBowled)
        assertEquals(6, over.ballLimit)
    }
}