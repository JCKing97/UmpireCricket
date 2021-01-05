package com.jcking97.umpirecricket

import org.junit.Test
import org.junit.Before

import org.junit.Assert.*

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
            assertEquals(i, over.currentBall)
            assertEquals(6, over.ballLimit)
        }
    }

    @Test
    fun whenExtraBallThenCurrentBallIncreasesAndBallLimitIncreases() {
        for (i in 1..3) {
            over.extraBall()
            assertEquals(i, over.currentBall)
            assertEquals(6+i, over.ballLimit)
        }
    }
}