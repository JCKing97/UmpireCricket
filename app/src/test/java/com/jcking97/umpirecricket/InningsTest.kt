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
        innings = Innings.newInnings()
    }

    fun assertBallsLimitOver(ballsBowled: Int, ballLimit: Int, oversBowled: Int) {
        assertEquals(ballsBowled, innings.getBallsBowled())
        assertEquals(ballLimit, innings.getBallLimit())
        assertEquals(oversBowled, innings.getOversBowled())
    }

    @Test
    fun whenNewInningsThenOversSetAndBallsSetAndLimitSet() {
        assertBallsLimitOver(0, 6, 0)
    }

    @Test
    fun whenBallsBowledThenCurrentBallIncreasedWhenBallsBowledReachesLimitThenNewOver() {
        for (over in 1..3) {
            for (ball in 1..5) {
                innings.ballBowledAndEndOverCheck()
                assertBallsLimitOver(ball, 6, over-1)
            }
            innings.ballBowledAndEndOverCheck()
            assertBallsLimitOver(0, 6, over)
        }
    }

    @Test
    fun whenExtraBallsThenBallLimitIncreasedAndCurrentBallIncreasedAndOverNotIncreased() {
        for (ball in 1..12) {
            innings.extraBall()
            assertBallsLimitOver(ball, ball+6, 0)
        }
    }

    @Test
    fun whenExtraBallsThenBallLimitIncreasedWhenBallsBowledThenCurrentBallIncreasedAndOverNotIncreasedUntilLimitReached() {
        val extraBalls = 13
        for (over in 1..3) {
            for (extraBall in 1..extraBalls) {
                innings.extraBall()
                assertBallsLimitOver(extraBall, extraBall+6, over-1)
            }
            for (ball in 1..5) {
                innings.ballBowledAndEndOverCheck()
                assertBallsLimitOver(extraBalls+ball, extraBalls+6, over-1)
            }
            innings.ballBowledAndEndOverCheck()
            assertBallsLimitOver(0, 6, over)
        }
    }

    @Test
    fun whenEndOverThenOversIncreased() {
        for (over in 1..12) {
            innings.endOver()
            assertBallsLimitOver(0, 6, over)
        }
    }

    @Test
    fun whenNewBallThenAddedWhenUndoThenRemoved() {
        for (ball in 1..5) {
            innings.ballBowledAndEndOverCheck()
            assertBallsLimitOver(1, 6, 0)
            innings.undoLastAction()
            assertBallsLimitOver(0, 6, 0)
        }
    }

    @Test
    fun whenNewBallMultipleThenAddedWhenUndoMultipleThenRemoved() {
        val balls = 5
        for (ball in 1..balls) {
            innings.ballBowledAndEndOverCheck()
            assertBallsLimitOver(ball, 6, 0)
        }
        for (ballRemoved in balls-1 downTo 0) {
            innings.undoLastAction()
            assertBallsLimitOver(ballRemoved, 6, 0)
        }
    }

    @Test
    fun whenExtraBallThenAddedWhenUndoThenRemoved(){
        for (ball in 1..5) {
            innings.extraBall()
            assertBallsLimitOver(1, 7, 0)
            innings.undoLastAction()
            assertBallsLimitOver(0, 6, 0)
        }
    }

    @Test
    fun whenExtraBallMultipleThenAddedWhenUndoMultipleThenRemoved() {
        val balls = 5
        for (ball in 1..balls) {
            innings.extraBall()
            assertBallsLimitOver(ball, 6+ball, 0)
            assertEquals(ball, innings.getBallsBowled())
            assertEquals(6+ball, innings.getBallLimit())
            assertEquals(0, innings.getOversBowled())
        }
        for (ballRemoved in balls-1 downTo 0) {
            innings.undoLastAction()
            assertBallsLimitOver(ballRemoved, 6+ballRemoved, 0)
        }
    }

    @Test
    fun whenNewOverThenAddedWhenUndoThenRemoved(){
        for (over in 1..5) {
            innings.endOver()
            assertBallsLimitOver(0, 6, 1)
            innings.undoLastAction()
            assertBallsLimitOver(0, 6, 0)
        }
    }

    @Test
    fun whenNewOverMultipleThenAddedWhenUndoMultipleThenRemoved() {
        val overs = 5
        for (over in 1..overs) {
            innings.endOver()
            assertBallsLimitOver(0, 6, over)
        }
        for (overRemoved in overs-1 downTo 0) {
            innings.undoLastAction()
            assertBallsLimitOver(0, 6, overRemoved)
        }
    }

    @Test
    fun whenBallsBowledThatBecomeOversThenAddedWhenUndoThenRemoved() {
        val overs = 5
        val balls = innings.getBallLimit()
        for (over in 1..overs) {
            for (ball in 1..balls) {
                assertBallsLimitOver(ball-1, 6, over-1)
                innings.ballBowledAndEndOverCheck()
            }
            assertBallsLimitOver(0, 6, over)
            innings.undoLastAction()
            assertBallsLimitOver(balls-1, 6, over-1)
            innings.ballBowledAndEndOverCheck()
        }
    }

    @Test
    fun whenBallsBowledThatBecomeOversMultipleThenAddedWhenUndoThenRemoved() {
        val overs = 5
        val balls = innings.getBallLimit()
        for (over in 1..overs) {
            for (ball in 1..balls) {
                assertBallsLimitOver(ball-1, 6, over-1)
                innings.ballBowledAndEndOverCheck()
            }
            assertBallsLimitOver(0, 6, over)
        }
        for (overRemoved in overs downTo 1) {
            assertBallsLimitOver(0, 6, overRemoved)
            for (ballRemoved in balls downTo 1) {
                innings.undoLastAction()
                assertBallsLimitOver(ballRemoved-1, 6, overRemoved-1)
            }
        }
    }

    @Test
    fun whenExtraBallsAndBallsBowledThenAddedWhenUndoThenRemoved() {
        val extraBalls = 3
        var extraBallsSoFar = 0
        for (ball in 1..5) {
            for (extraBall in 1..extraBalls) {
                innings.extraBall()
                extraBallsSoFar++
                assertBallsLimitOver((ball-1)+extraBallsSoFar, 6+extraBallsSoFar, 0)
                innings.undoLastAction()
                assertBallsLimitOver((ball-1)+extraBallsSoFar-1, 6+extraBallsSoFar-1, 0)
                innings.extraBall()
            }
            innings.ballBowledAndEndOverCheck()
            assertBallsLimitOver(ball+extraBallsSoFar, 6+extraBallsSoFar, 0)
            innings.undoLastAction()
            assertBallsLimitOver(ball+extraBallsSoFar-1, 6+extraBallsSoFar, 0)
            innings.ballBowledAndEndOverCheck()
        }
    }

    @Test
    fun whenExtraBallsAndBallsBowledSequentiallyThenAddedWhenUndoSequentiallyThenRemoved() {
        val extraBalls = 3
        val balls = 5
        var extraBallsSoFar = 0
        for (ball in 1..balls) {
            for (extraBall in 1..extraBalls) {
                innings.extraBall()
                extraBallsSoFar++
                assertBallsLimitOver((ball-1)+extraBallsSoFar, 6+extraBallsSoFar, 0)
            }
            innings.ballBowledAndEndOverCheck()
            assertBallsLimitOver(ball+extraBallsSoFar, 6+extraBallsSoFar, 0)
        }
        for (ballRemoved in balls downTo 1) {
            assertBallsLimitOver(ballRemoved+extraBallsSoFar, 6+extraBallsSoFar, 0)
            innings.undoLastAction()
            for (extraBall in 1..extraBalls) {
                innings.undoLastAction()
                extraBallsSoFar--
                assertBallsLimitOver((ballRemoved-1)+extraBallsSoFar, 6+extraBallsSoFar, 0)
            }
        }
    }

    @Test
    fun whenExtraBallsAndNewOversThenAddedWhenUndoThenRemoved() {
        val extraBalls = 7
        val overs = 3
        var oversSoFar = 0
        for (extraBall in 1..extraBalls) {
            innings.extraBall()
            assertBallsLimitOver(1, 6+1, oversSoFar)
            innings.undoLastAction()
            assertBallsLimitOver(0, 6, oversSoFar)
            innings.extraBall()
            for (over in 1..overs) {
                innings.endOver()
                oversSoFar++
                assertBallsLimitOver(0, 6, oversSoFar)
                innings.undoLastAction()
                // When we undo the first over it goes back to an over with an extra ball in
                // When we undo subsequent overs, they have no extra ball
                val expectedAddition = if (over == 1) 1 else 0
                assertBallsLimitOver(expectedAddition, 6+expectedAddition, oversSoFar-1)
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
            innings.extraBall()
            assertBallsLimitOver(1, 6+1, oversSoFar)
            for (over in 1..overs) {
                innings.endOver()
                oversSoFar++
                assertBallsLimitOver(0, 6, oversSoFar)
            }
        }
        for (extraBallRemoved in extraBalls downTo 1) {
            for (overRemoved in overs downTo 1) {
                innings.undoLastAction()
                oversSoFar--
                // When we undo the first over it goes back to an over with an extra ball in
                // When we undo subsequent overs, they have no extra ball
                val expectedAddition = if (overRemoved == 1) 1 else 0
                assertBallsLimitOver(expectedAddition, 6+expectedAddition, oversSoFar)
            }
            innings.undoLastAction()
            assertBallsLimitOver(0, 6, oversSoFar)
        }
    }

    @Test
    fun whenCombinationThenAddedWhenUndoThenRemoved() {
        var ballsSoFar = 0
        var extraBallsSoFar = 0
        var oversSoFar = 0
        for (over in 1..5) {
            for (balls in 1..3) {
                innings.ballBowledAndEndOverCheck()
                ballsSoFar++
                assertBallsLimitOver(ballsSoFar, 6, oversSoFar)
                innings.undoLastAction()
                assertBallsLimitOver(ballsSoFar-1, 6, oversSoFar)
                innings.ballBowledAndEndOverCheck()
            }
            for (extraBall in 1..7) {
                innings.extraBall()
                extraBallsSoFar++
                assertBallsLimitOver(ballsSoFar + extraBallsSoFar, 6 + extraBallsSoFar, oversSoFar)
                innings.undoLastAction()
                assertBallsLimitOver(ballsSoFar + extraBallsSoFar-1, 6 + extraBallsSoFar-1, oversSoFar)
                innings.extraBall()
            }
            for (balls in 1..2) {
                innings.ballBowledAndEndOverCheck()
                ballsSoFar++
                assertBallsLimitOver(ballsSoFar + extraBallsSoFar, 6 + extraBallsSoFar, oversSoFar)
                innings.undoLastAction()
                assertBallsLimitOver(ballsSoFar + extraBallsSoFar-1, 6 + extraBallsSoFar, oversSoFar)
                innings.ballBowledAndEndOverCheck()
            }
            innings.ballBowledAndEndOverCheck()
            ballsSoFar++
            oversSoFar++
            assertBallsLimitOver(0, 6, oversSoFar)
            innings.undoLastAction()
            assertBallsLimitOver(ballsSoFar + extraBallsSoFar-1, 6 + extraBallsSoFar, oversSoFar-1)
            innings.ballBowledAndEndOverCheck()
            innings.endOver()
            oversSoFar++
            assertBallsLimitOver(0, 6, oversSoFar)
            innings.undoLastAction()
            assertBallsLimitOver(0, 6, oversSoFar-1)
            innings.endOver()
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
                innings.ballBowledAndEndOverCheck()
                ballsSoFar++
                assertBallsLimitOver(ballsSoFar, 6, oversSoFar)
            }
            for (extraBall in 1..extraBalls) {
                innings.extraBall()
                extraBallsSoFar++
                assertBallsLimitOver(ballsSoFar + extraBallsSoFar, 6 + extraBallsSoFar, oversSoFar)
            }
            for (balls in 1..balls2) {
                innings.ballBowledAndEndOverCheck()
                ballsSoFar++
                assertBallsLimitOver(ballsSoFar + extraBallsSoFar, 6 + extraBallsSoFar, oversSoFar)
            }
            innings.ballBowledAndEndOverCheck()
            ballsSoFar++
            oversSoFar++
            assertBallsLimitOver(0, 6, oversSoFar)
            innings.endOver()
            oversSoFar++
            assertBallsLimitOver(0, 6, oversSoFar)
            ballsSoFar = 0
            extraBallsSoFar = 0
        }
        for (overRemoved in overs downTo 1) {
            assertBallsLimitOver(0, 6, oversSoFar)
            // Undo end over
            innings.undoLastAction()
            oversSoFar--
            // Undo last ball of over bowled
            assertBallsLimitOver(0, 6, oversSoFar)
            innings.undoLastAction()
            oversSoFar--
            ballsSoFar = balls1 + balls2
            extraBallsSoFar = extraBalls
            for (balls in 1..balls2) {
                assertBallsLimitOver(ballsSoFar + extraBallsSoFar, 6 + extraBallsSoFar, oversSoFar)
                innings.undoLastAction()
                ballsSoFar--
            }
            for (extraBall in 1..extraBalls) {
                assertBallsLimitOver(ballsSoFar + extraBallsSoFar, 6 + extraBallsSoFar, oversSoFar)
                innings.undoLastAction()
                extraBallsSoFar--
            }
            for (balls in 1..balls1) {
                assertBallsLimitOver(ballsSoFar, 6, oversSoFar)
                innings.undoLastAction()
                ballsSoFar--
            }
        }
    }

}