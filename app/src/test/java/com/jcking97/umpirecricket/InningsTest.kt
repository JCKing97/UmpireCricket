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
        assertEquals(0, innings.getBallsBowled())
        assertEquals(0, innings.getOversBowled())
    }

    @Test
    fun whenBallsBowledThenCurrentBallIncreasedWhenBallsBowledReachesLimitThenNewOver() {
        for (over in 1..3) {
            for (ball in 1..5) {
                innings.ballBowled()
                assertEquals(ball, innings.getBallsBowled())
                assertEquals(6, innings.getBallLimit())
                assertEquals(over-1, innings.getOversBowled())
            }
            innings.ballBowled()
            assertEquals(0, innings.getBallsBowled())
            assertEquals(6, innings.getBallLimit())
            assertEquals(over, innings.getOversBowled())
        }
    }

    @Test
    fun whenExtraBallsThenBallLimitIncreasedAndCurrentBallIncreasedAndOverNotIncreased() {
        for (ball in 1..12) {
            innings.extraBall()
            assertEquals(ball, innings.getBallsBowled())
            assertEquals(ball+6, innings.getBallLimit())
            assertEquals(0, innings.getOversBowled())
        }
    }

    @Test
    fun whenExtraBallsThenBallLimitIncreasedWhenBallsBowledThenCurrentBallIncreasedAndOverNotIncreasedUntilLimitReached() {
        val extraBalls = 6
        for (over in 1..3) {
            for (extraBall in 1..extraBalls) {
                innings.extraBall()
                assertEquals(extraBall, innings.getBallsBowled())
                assertEquals(extraBall+6, innings.getBallLimit())
                assertEquals(over-1, innings.getOversBowled())
            }
            for (ball in 1..5) {
                innings.ballBowled()
                assertEquals(extraBalls+ball, innings.getBallsBowled())
                assertEquals(extraBalls+6, innings.getBallLimit())
                assertEquals(over-1, innings.getOversBowled())
            }
            innings.ballBowled()
            assertEquals(0, innings.getBallsBowled())
            assertEquals(6, innings.getBallLimit())
            assertEquals(over, innings.getOversBowled())
        }
    }

    @Test
    fun whenEndOverThenOversIncreased() {
        for (over in 1..12) {
            innings.endOver()
            assertEquals(0, innings.getBallsBowled())
            assertEquals(6, innings.getBallLimit())
            assertEquals(over, innings.getOversBowled())
        }
    }

}