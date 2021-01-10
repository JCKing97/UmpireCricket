package com.jcking97.umpirecricket

import com.jcking97.umpirecricket.Utilities.assertBallsLimitOver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BallBowledEventTest {

    private lateinit var innings: Innings
    private lateinit var ballBowledEvent: BallBowledEvent

    @Before
    fun setUp() {
        innings = Innings()
        ballBowledEvent = BallBowledEvent(innings)
    }

    @Test
    fun whenBallsBowledThenCurrentBallIncreased() {
        for (ball in 1..5) {
            val endOverEvent = ballBowledEvent.doEvent()
            assertNull(endOverEvent)
            assertBallsLimitOver(innings, ball, 6, 0)
        }
    }

    @Test
    fun whenBallsBowledAndBallsBowledReachesLimitThenNewOver() {
        for (over in 1..3) {
            for (ball in 1..5) {
                ballBowledEvent.doEvent()
            }
            val endOverEvent = ballBowledEvent.doEvent()
            assertNotNull(endOverEvent)
            endOverEvent!!.doEvent()
            assertBallsLimitOver(innings, 0, 6, over)
        }
    }

    @Test
    fun whenNewBallThenAddedWhenUndoThenRemoved() {
        for (ball in 1..5) {
            val endOverEvent = ballBowledEvent.doEvent()
            assertNull(endOverEvent)
            assertBallsLimitOver(innings, 1, 6, 0)
            val eventCausedByAnother = ballBowledEvent.doEventInverse()
            assertFalse(eventCausedByAnother)
            assertBallsLimitOver(innings, 0, 6, 0)
        }
    }

    @Test
    fun whenMultipleNewBallsAddedWhenUndoThenRemoved() {
        for (ball in 1..5) {
            val endOverEvent = ballBowledEvent.doEvent()
            assertNull(endOverEvent)
            assertBallsLimitOver(innings, ball, 6, 0)
        }
        for (ball in 4 downTo 0) {
            val eventCausedByAnother = ballBowledEvent.doEventInverse()
            assertFalse(eventCausedByAnother)
            assertBallsLimitOver(innings, ball, 6, 0)
        }
    }

    @Test
    fun whenBallsBowledThatBecomeOversMultipleThenAddedWhenUndoThenRemoved() {
        val overs = 5
        val balls = innings.getCurrentOver().ballLimit
        val endOverEvents = mutableListOf<Event>()
        for (over in 1..overs) {
            var endOverEvent: Event? = null
            for (ball in 1..balls) {
                endOverEvent = ballBowledEvent.doEvent()
                assertBallsLimitOver(innings, ball, 6, over - 1)
            }
            assertNotNull(endOverEvent)
            endOverEvents.add(endOverEvent!!)
            endOverEvent.doEvent()
            assertBallsLimitOver(innings, 0, 6, over)
        }
        for (overRemoved in overs downTo 1) {
            assertBallsLimitOver(innings, 0, 6, overRemoved)
            endOverEvents.removeFirst().doEventInverse()
            for (ballRemoved in balls downTo 1) {
                ballBowledEvent.doEventInverse()
                assertBallsLimitOver(innings, ballRemoved - 1, 6, overRemoved - 1)
            }
        }
    }
}