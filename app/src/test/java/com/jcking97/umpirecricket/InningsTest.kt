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

}