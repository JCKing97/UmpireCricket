package com.jcking97.umpirecricket

import org.junit.Test
import org.junit.Before

import org.junit.Assert.*

/**
 * Tests for the Innings class.
 */
class InningsTest {

    private lateinit var innings: Innings

    @Before
    fun setUp() {
        innings = Innings()
    }

    @Test
    fun whenNewInningsThenOversSetAndBallsSetAndLimitSet() {
        assertEquals(0, innings.getCurrentOver().ballsBowled)
        assertEquals(6, innings.getCurrentOver().ballLimit)
        assertEquals(0, innings.getOversBowled())
    }

    @Test
    fun whenEndOverThenNewOver() {
        val firstOver = innings.getCurrentOver()
        innings.endOver()
        assertEquals(innings.getOversBowled(), 1)
        assertNotEquals(firstOver, innings.getCurrentOver())
    }

    @Test
    fun whenEndOverAndUndoEndOverThenNoNewOver() {
        val firstOver = innings.getCurrentOver()
        innings.endOver()
        innings.undoEndOver()
        assertEquals(innings.getOversBowled(), 0)
        assertEquals(firstOver, innings.getCurrentOver())
    }

    @Test
    fun whenUndoEndOverThenOverTheSame() {
        val firstOver = innings.getCurrentOver()
        innings.undoEndOver()
        assertEquals(innings.getOversBowled(), 0)
        assertEquals(firstOver, innings.getCurrentOver())
    }

}
