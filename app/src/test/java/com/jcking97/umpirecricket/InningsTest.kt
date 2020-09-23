package com.jcking97.umpirecricket

import org.junit.Test
import org.junit.Before

import org.junit.Assert.*

/**
 * Tests for the Over class.
 */
class InningsTest {

    private lateinit var innings: Innings

    @Before
    fun setUp() {
        innings = Innings.newInnings()
    }

    @Test
    fun whenNewInningsThenOversSetAndBallsSetAndLimitSet() {
        assertEquals(6, innings.getBallLimit())
        assertEquals(0, innings.getCurrentBall())
        assertEquals(0, innings.getOverCount())
    }

    @Test
    fun whenBallsBowledThenCurrentBallIncreasedWhenBallsBowledReachesLimitThenNewOver() {
        for (over in 1..3) {
            for (ball in 1..5) {
                innings.ballBowled()
                assertEquals(ball, innings.getCurrentBall())
                assertEquals(6, innings.getBallLimit())
                assertEquals(over-1, innings.getOverCount())
            }
            innings.ballBowled()
            assertEquals(0, innings.getCurrentBall())
            assertEquals(6, innings.getBallLimit())
            assertEquals(over, innings.getOverCount())
        }
    }

    @Test
    fun whenExtraBallsThenBallLimitIncreasedAndCurrentBallIncreasedAndOverNotIncreased() {
        for (ball in 1..12) {
            innings.extraBall()
            assertEquals(ball, innings.getCurrentBall())
            assertEquals(ball+6, innings.getBallLimit())
            assertEquals(0, innings.getOverCount())
        }
    }

    @Test
    fun whenExtraBallsThenBallLimitIncreasedWhenBallsBowledThenCurrentBallIncreasedAndOverNotIncreasedUntilLimitReached() {
        val extraBalls: Int = 6
        for (over in 1..3) {
            for (extraBall in 1..extraBalls) {
                innings.extraBall()
                assertEquals(extraBall, innings.getCurrentBall())
                assertEquals(extraBall+6, innings.getBallLimit())
                assertEquals(over-1, innings.getOverCount())
            }
            for (ball in 1..5) {
                innings.ballBowled()
                assertEquals(extraBalls+ball, innings.getCurrentBall())
                assertEquals(extraBalls+6, innings.getBallLimit())
                assertEquals(over-1, innings.getOverCount())
            }
            innings.ballBowled()
            assertEquals(0, innings.getCurrentBall())
            assertEquals(6, innings.getBallLimit())
            assertEquals(over, innings.getOverCount())
        }
    }

    @Test
    fun whenEndOverThenOversIncreased() {
        for (over in 1..12) {
            innings.endOver()
            assertEquals(0, innings.getCurrentBall())
            assertEquals(6, innings.getBallLimit())
            assertEquals(over, innings.getOverCount())
        }
    }

}