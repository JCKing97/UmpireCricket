package com.jcking97.umpirecricket

import com.jcking97.umpirecricket.Utilities.assertBallsLimitOver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EndOverEventTest {

    private lateinit var innings: Innings
    private lateinit var endOverEventCausedByBallBowled: EndOverEvent
    private lateinit var endOverEventNotCausedByBallBowled: EndOverEvent

    @Before
    fun setUp() {
        innings = Innings()
        endOverEventNotCausedByBallBowled = EndOverEvent(innings, false)
        endOverEventCausedByBallBowled = EndOverEvent(innings, true)
    }

    @Test
    fun whenEndOverThenOversIncreased() {
        for (over in 1..12) {
            val newEvent = endOverEventNotCausedByBallBowled.doEvent()
            assertNull(newEvent)
            assertBallsLimitOver(innings, 0, 6, over)
        }
    }

    @Test
    fun whenEndOverCausedByPreviousEventThenOversIncreased() {
        for (over in 1..12) {
            val newEvent = endOverEventCausedByBallBowled.doEvent()
            assertNull(newEvent)
            assertBallsLimitOver(innings, 0, 6, over)
        }
    }

    @Test
    fun whenNewOverThenAddedWhenUndoThenRemoved(){
        for (over in 1..5) {
            val newEvent = endOverEventNotCausedByBallBowled.doEvent()
            assertNull(newEvent)
            assertBallsLimitOver(innings, 0, 6, 1)
            val causedByPreviousEvent = endOverEventNotCausedByBallBowled.doEventInverse()
            assertFalse(causedByPreviousEvent)
            assertBallsLimitOver(innings, 0, 6, 0)
        }
    }

    @Test
    fun whenNewOverMultipleThenAddedWhenUndoMultipleThenRemoved() {
        val overs = 5
        for (over in 1..overs) {
            val newEvent = endOverEventNotCausedByBallBowled.doEvent()
            assertNull(newEvent)
            assertBallsLimitOver(innings, 0, 6, over)
        }
        for (overRemoved in overs-1 downTo 0) {
            val causedByPreviousEvent = endOverEventNotCausedByBallBowled.doEventInverse()
            assertFalse(causedByPreviousEvent)
            assertBallsLimitOver(innings, 0, 6, overRemoved)
        }
    }

    @Test
    fun whenNewOverCausedByPreviousEventThenAddedWhenUndoThenRemoved(){
        for (over in 1..5) {
            val newEvent = endOverEventCausedByBallBowled.doEvent()
            assertNull(newEvent)
            assertBallsLimitOver(innings, 0, 6, 1)
            val causedByPreviousEvent = endOverEventCausedByBallBowled.doEventInverse()
            assertTrue(causedByPreviousEvent)
            assertBallsLimitOver(innings, 0, 6, 0)
        }
    }

    @Test
    fun whenNewOverCausedByPreviousEventMultipleThenAddedWhenUndoMultipleThenRemoved() {
        val overs = 5
        for (over in 1..overs) {
            val newEvent = endOverEventCausedByBallBowled.doEvent()
            assertNull(newEvent)
            assertBallsLimitOver(innings, 0, 6, over)
        }
        for (overRemoved in overs-1 downTo 0) {
            val causedByPreviousEvent = endOverEventCausedByBallBowled.doEventInverse()
            assertTrue(causedByPreviousEvent)
            assertBallsLimitOver(innings, 0, 6, overRemoved)
        }
    }

}