package com.jcking97.umpirecricket

import com.jcking97.umpirecricket.Utilities.assertBallsLimitOver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EventsTest {

    private lateinit var innings: Innings
    private lateinit var events: Events
    private lateinit var extraBallEvent: ExtraBallEvent
    private lateinit var ballBowledEvent: BallBowledEvent
    private lateinit var endOverEventCausedByBallBowled: EndOverEvent
    private lateinit var endOverEventNotCausedByBallBowled: EndOverEvent

    @Before
    fun setUp() {
        innings = Innings()
        events = Events()
        extraBallEvent = ExtraBallEvent(innings)
        ballBowledEvent = BallBowledEvent(innings)
        endOverEventNotCausedByBallBowled = EndOverEvent(innings, false)
        endOverEventCausedByBallBowled = EndOverEvent(innings, true)
    }

    @Test
    fun whenExtraBallsThenBallLimitIncreasedWhenBallsBowledThenCurrentBallIncreasedAndOverNotIncreasedUntilLimitReached() {
        val extraBalls = 13
        for (over in 1..3) {
            for (extraBall in 1..extraBalls) {
                events.executeEvent(extraBallEvent)
                assertBallsLimitOver(innings, extraBall, extraBall+6, over-1)
            }
            for (ball in 1..5) {
                events.executeEvent(ballBowledEvent)
                assertBallsLimitOver(innings, extraBalls+ball, extraBalls+6, over-1)
            }
            events.executeEvent(ballBowledEvent)
            assertBallsLimitOver(innings, 0, 6, over)
        }
    }

    @Test
    fun whenBallsBowledThatBecomeOversThenAddedWhenUndoThenRemoved() {
        val overs = 5
        val balls = innings.getCurrentOver().ballLimit
        for (over in 1..overs) {
            for (ball in 1..balls) {
                assertBallsLimitOver(innings, ball - 1, 6, over - 1)
                events.executeEvent(ballBowledEvent)
            }
            assertBallsLimitOver(innings, 0, 6, over)
            events.undoLastEventAndCausingEvents()
            assertBallsLimitOver(innings, balls-1, 6, over-1)
            events.executeEvent(ballBowledEvent)
        }
    }

    @Test
    fun whenExtraBallsAndBallsBowledThenAddedWhenUndoThenRemoved() {
        val extraBalls = 3
        var extraBallsSoFar = 0
        for (ball in 1..5) {
            for (extraBall in 1..extraBalls) {
                events.executeEvent(extraBallEvent)
                extraBallsSoFar++
                assertBallsLimitOver(innings, (ball-1)+extraBallsSoFar, 6+extraBallsSoFar, 0)
                events.undoLastEventAndCausingEvents()
                assertBallsLimitOver(innings, (ball-1)+extraBallsSoFar-1, 6+extraBallsSoFar-1, 0)
                events.executeEvent(extraBallEvent)
            }
            events.executeEvent(ballBowledEvent)
            assertBallsLimitOver(innings, ball+extraBallsSoFar, 6+extraBallsSoFar, 0)
            events.undoLastEventAndCausingEvents()
            assertBallsLimitOver(innings,ball+extraBallsSoFar-1, 6+extraBallsSoFar, 0)
            events.executeEvent(ballBowledEvent)
        }
    }

    @Test
    fun whenExtraBallsAndBallsBowledSequentiallyThenAddedWhenUndoSequentiallyThenRemoved() {
        val extraBalls = 3
        val balls = 5
        var extraBallsSoFar = 0
        for (ball in 1..balls) {
            for (extraBall in 1..extraBalls) {
                events.executeEvent(extraBallEvent)
                extraBallsSoFar++
                assertBallsLimitOver(innings, (ball-1)+extraBallsSoFar, 6+extraBallsSoFar, 0)
            }
            events.executeEvent(ballBowledEvent)
            assertBallsLimitOver(innings, ball+extraBallsSoFar, 6+extraBallsSoFar, 0)
        }
        for (ballRemoved in balls downTo 1) {
            assertBallsLimitOver(innings, ballRemoved+extraBallsSoFar, 6+extraBallsSoFar, 0)
            events.undoLastEventAndCausingEvents()
            for (extraBall in 1..extraBalls) {
                events.undoLastEventAndCausingEvents()
                extraBallsSoFar--
                assertBallsLimitOver(innings, (ballRemoved-1)+extraBallsSoFar, 6+extraBallsSoFar, 0)
            }
        }
    }

    @Test
    fun whenExtraBallsAndNewOversThenAddedWhenUndoThenRemoved() {
        val extraBalls = 7
        val overs = 3
        var oversSoFar = 0
        for (extraBall in 1..extraBalls) {
            events.executeEvent(extraBallEvent)
            assertBallsLimitOver(innings, 1, 6+1, oversSoFar)
            events.undoLastEventAndCausingEvents()
            assertBallsLimitOver(innings, 0, 6, oversSoFar)
            events.executeEvent(extraBallEvent)
            for (over in 1..overs) {
                events.executeEvent(endOverEventNotCausedByBallBowled)
                oversSoFar++
                assertBallsLimitOver(innings, 0, 6, oversSoFar)
                events.undoLastEventAndCausingEvents()
                // When we undo the first over it goes back to an over with an extra ball in
                // When we undo subsequent overs, they have no extra ball
                val expectedAddition = if (over == 1) 1 else 0
                assertBallsLimitOver(
                    innings,
                    expectedAddition, 6+expectedAddition, oversSoFar-1
                )
                innings.endOver()
            }
        }
    }

    @Test
    fun whenExtraBallsAndNewOversSequentialThenAddedWhenUndoSequentialThenRemoved() {
        val extraBalls = 7
        val overs = 3
        var oversSoFar = 0
        for (extraBall in 1..extraBalls) {
            events.executeEvent(extraBallEvent)
            assertBallsLimitOver(innings, 1, 6+1, oversSoFar)
            for (over in 1..overs) {
                events.executeEvent(endOverEventNotCausedByBallBowled)
                oversSoFar++
                assertBallsLimitOver(innings, 0, 6, oversSoFar)
            }
        }
        for (extraBallRemoved in extraBalls downTo 1) {
            for (overRemoved in overs downTo 1) {
                events.undoLastEventAndCausingEvents()
                oversSoFar--
                // When we undo the first over it goes back to an over with an extra ball in
                // When we undo subsequent overs, they have no extra ball
                val expectedAddition = if (overRemoved == 1) 1 else 0
                assertBallsLimitOver(innings, expectedAddition, 6+expectedAddition, oversSoFar)
            }
            events.undoLastEventAndCausingEvents()
            assertBallsLimitOver(innings, 0, 6, oversSoFar)
        }
    }

    @Test
    fun whenCombinationThenAddedWhenUndoThenRemoved() {
        var ballsSoFar = 0
        var extraBallsSoFar = 0
        var oversSoFar = 0
        for (over in 1..5) {
            for (balls in 1..3) {
                events.executeEvent(ballBowledEvent)
                ballsSoFar++
                assertBallsLimitOver(innings, ballsSoFar, 6, oversSoFar)
                events.undoLastEventAndCausingEvents()
                assertBallsLimitOver(innings, ballsSoFar-1, 6, oversSoFar)
                events.executeEvent(ballBowledEvent)
            }
            for (extraBall in 1..7) {
                events.executeEvent(extraBallEvent)
                extraBallsSoFar++
                assertBallsLimitOver(innings, ballsSoFar + extraBallsSoFar, 6 + extraBallsSoFar, oversSoFar)
                events.undoLastEventAndCausingEvents()
                assertBallsLimitOver(innings, ballsSoFar + extraBallsSoFar-1, 6 + extraBallsSoFar-1, oversSoFar)
                events.executeEvent(extraBallEvent)
            }
            for (balls in 1..2) {
                events.executeEvent(ballBowledEvent)
                ballsSoFar++
                assertBallsLimitOver(innings, ballsSoFar + extraBallsSoFar, 6 + extraBallsSoFar, oversSoFar)
                events.undoLastEventAndCausingEvents()
                assertBallsLimitOver(innings, ballsSoFar + extraBallsSoFar-1, 6 + extraBallsSoFar, oversSoFar)
                events.executeEvent(ballBowledEvent)
            }
            events.executeEvent(ballBowledEvent)
            ballsSoFar++
            oversSoFar++
            assertBallsLimitOver(innings, 0, 6, oversSoFar)
            events.undoLastEventAndCausingEvents()
            assertBallsLimitOver(innings, ballsSoFar + extraBallsSoFar-1, 6 + extraBallsSoFar, oversSoFar-1)
            events.executeEvent(ballBowledEvent)
            events.executeEvent(endOverEventNotCausedByBallBowled)
            oversSoFar++
            assertBallsLimitOver(innings, 0, 6, oversSoFar)
            events.undoLastEventAndCausingEvents()
            assertBallsLimitOver(innings,0, 6, oversSoFar-1)
            events.executeEvent(endOverEventNotCausedByBallBowled)
            ballsSoFar = 0
            extraBallsSoFar = 0
        }
    }

    @Test
    fun whenCombinationSequentialThenAddedWhenUndoSequentialThenRemoved(){
        var ballsSoFar = 0
        var extraBallsSoFar = 0
        var oversSoFar = 0
        val overs = 5
        val balls1 = 3
        val balls2 = 2
        val extraBalls = 7
        for (over in 1..overs) {
            for (balls in 1..balls1) {
                events.executeEvent(ballBowledEvent)
                ballsSoFar++
                assertBallsLimitOver(innings, ballsSoFar, 6, oversSoFar)
            }
            for (extraBall in 1..extraBalls) {
                events.executeEvent(extraBallEvent)
                extraBallsSoFar++
                assertBallsLimitOver(innings, ballsSoFar + extraBallsSoFar, 6 + extraBallsSoFar, oversSoFar)
            }
            for (balls in 1..balls2) {
                events.executeEvent(ballBowledEvent)
                ballsSoFar++
                assertBallsLimitOver(innings, ballsSoFar + extraBallsSoFar, 6 + extraBallsSoFar, oversSoFar)
            }
            events.executeEvent(ballBowledEvent)
            oversSoFar++
            assertBallsLimitOver(innings, 0, 6, oversSoFar)
            events.executeEvent(endOverEventNotCausedByBallBowled)
            oversSoFar++
            assertBallsLimitOver(innings, 0, 6, oversSoFar)
            ballsSoFar = 0
            extraBallsSoFar = 0
        }
        for (overRemoved in overs downTo 1) {
            assertBallsLimitOver(innings, 0, 6, oversSoFar)
            // Undo end over
            events.undoLastEventAndCausingEvents()
            oversSoFar--
            // Undo last ball of over bowled
            assertBallsLimitOver(innings, 0, 6, oversSoFar)
            events.undoLastEventAndCausingEvents()
            oversSoFar--
            ballsSoFar = balls1 + balls2
            extraBallsSoFar = extraBalls
            for (balls in 1..balls2) {
                assertBallsLimitOver(innings, ballsSoFar + extraBallsSoFar, 6 + extraBallsSoFar, oversSoFar)
                events.undoLastEventAndCausingEvents()
                ballsSoFar--
            }
            for (extraBall in 1..extraBalls) {
                assertBallsLimitOver(innings, ballsSoFar + extraBallsSoFar, 6 + extraBallsSoFar, oversSoFar)
                events.undoLastEventAndCausingEvents()
                extraBallsSoFar--
            }
            for (balls in 1..balls1) {
                assertBallsLimitOver(innings, ballsSoFar, 6, oversSoFar)
                events.undoLastEventAndCausingEvents()
                ballsSoFar--
            }
        }
    }
}
