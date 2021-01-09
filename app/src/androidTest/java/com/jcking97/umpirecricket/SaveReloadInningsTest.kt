package com.jcking97.umpirecricket

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.File


/**
 * Test the Innings' classes save and reload from file feature.
 */
@RunWith(AndroidJUnit4::class)
class SaveReloadInningsTest {

    private lateinit var appContext: Context

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // Remove all saved files
        appContext.filesDir.list { dir, name -> File(dir, name).delete() }
    }

    fun assertBallsLimitOver(innings: Innings, ballsBowled: Int, ballLimit: Int, oversBowled: Int) {
        assertEquals(ballsBowled, innings.getBallsBowled())
        assertEquals(ballLimit, innings.getBallLimit())
        assertEquals(oversBowled, innings.getOversBowled())
    }

    @Test
    fun testGivenNoPreviousSaveWhenReloadFromFileThenActionsPreserved() {
        val innings = Innings.fromFile(appContext)
        assertBallsLimitOver(innings, 0, 6, 0)
    }

    @Test
    fun testGivenBallBowledWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val innings = Innings.newInnings()
        innings.ballBowledAndEndOverCheck()
        assertBallsLimitOver(innings, 1, 6, 0)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, 1, 6, 0)
    }

    @Test
    fun testGivenExtraBallWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val innings = Innings.newInnings()
        innings.extraBall()
        assertBallsLimitOver(innings, 1, 7, 0)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, 1, 7, 0)
    }

    @Test
    fun testGivenEndOverWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val innings = Innings.newInnings()
        innings.endOver()
        assertBallsLimitOver(innings, 0, 6, 1)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, 0, 6, 1)
    }

    @Test
    fun testGivenBallBowledWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenActionsRemoved() {
        val innings = Innings.newInnings()
        innings.ballBowledAndEndOverCheck()
        assertBallsLimitOver(innings, 1, 6, 0)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, 1, 6, 0)
        newInnings.undoLastAction()
        assertBallsLimitOver(newInnings, 0, 6, 0)
    }

    @Test
    fun testGivenExtraBallWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenActionsRemoved() {
        val innings = Innings.newInnings()
        innings.extraBall()
        assertBallsLimitOver(innings, 1, 7, 0)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, 1, 7, 0)
        newInnings.undoLastAction()
        assertBallsLimitOver(newInnings, 0, 6, 0)
    }

    @Test
    fun testGivenEndOverWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenActionsRemoved() {
        val innings = Innings.newInnings()
        innings.endOver()
        assertBallsLimitOver(innings, 0, 6, 1)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, 0, 6, 1)
        newInnings.undoLastAction()
        assertBallsLimitOver(newInnings, 0, 6, 0)
    }

    @Test
    fun testGivenBallsBowledAndOneUndoWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val innings = Innings.newInnings()
        for (ball in 1..2) {
            innings.ballBowledAndEndOverCheck()
        }
        innings.undoLastAction()
        assertBallsLimitOver(innings, 1, 6, 0)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, 1, 6, 0)
    }

    @Test
    fun testGivenExtraBallsAndOneUndoWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val innings = Innings.newInnings()
        for (extraBall in 1..2) {
            innings.extraBall()
        }
        innings.undoLastAction()
        assertBallsLimitOver(innings, 1, 7, 0)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, 1, 7, 0)
    }

    @Test
    fun testGivenEndOverTwiceAndUndoOnceWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val innings = Innings.newInnings()
        for (over in 1..2) {
            innings.endOver()
        }
        innings.undoLastAction()
        assertBallsLimitOver(innings, 0, 6, 1)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, 0, 6, 1)
    }

    @Test
    fun testGivenBallsBowledAndOneUndoWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenActionsRemoved() {
        val innings = Innings.newInnings()
        for (ball in 1..2) {
            innings.ballBowledAndEndOverCheck()
        }
        assertBallsLimitOver(innings, 2, 6, 0)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, 2, 6, 0)
        newInnings.undoLastAction()
        assertBallsLimitOver(newInnings, 1, 6, 0)
    }

    @Test
    fun testGivenExtraBallsAndOneUndoWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenActionsRemoved() {
        val innings = Innings.newInnings()
        for (extraBall in 1..2) {
            innings.extraBall()
        }
        assertBallsLimitOver(innings, 2, 8, 0)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, 2, 8, 0)
        newInnings.undoLastAction()
        assertBallsLimitOver(newInnings, 1, 7, 0)
    }

    @Test
    fun testGivenEndOverTwiceAndUndoOnceWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenActionsRemoved() {
        val innings = Innings.newInnings()
        for (over in 1..2) {
            innings.endOver()
        }
        assertBallsLimitOver(innings, 0, 6, 2)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, 0, 6, 2)
        newInnings.undoLastAction()
        assertBallsLimitOver(newInnings, 0, 6, 1)
    }

    @Test
    fun  testGivenActionCombinationWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val innings = Innings.newInnings()
        val balls = 9
        var ballsSoFar = 0
        val extraBalls = 3
        var extraBallsSoFar = 0
        val overs = 5
        var oversSoFar = 0
        for (over in 1..overs) {
            innings.endOver()
            oversSoFar++
            ballsSoFar = 0
            extraBallsSoFar = 0
            for (balls in 1..balls) {
                innings.ballBowledAndEndOverCheck()
                ballsSoFar++
            }
            oversSoFar += ballsSoFar / 6
            ballsSoFar %= 6
            for (extraBalls in 1..extraBalls) {
                innings.extraBall()
                extraBallsSoFar++
            }
        }
        assertBallsLimitOver(innings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
    }

    @Test
    fun  testGivenActionCombinationWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenRemoved() {
        val innings = Innings.newInnings()
        val balls = 9
        var ballsSoFar = 0
        val extraBalls = 3
        var extraBallsSoFar = 0
        val overs = 5
        var oversSoFar = 0
        for (over in 1..overs) {
            innings.endOver()
            oversSoFar++
            ballsSoFar = 0
            extraBallsSoFar = 0
            for (ball in 1..balls) {
                innings.ballBowledAndEndOverCheck()
                ballsSoFar++
            }
            oversSoFar += ballsSoFar / 6
            ballsSoFar %= 6
            for (extraBall in 1..extraBalls) {
                innings.extraBall()
                extraBallsSoFar++
            }
        }
        assertBallsLimitOver(innings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
        for (overRemoved in overs downTo 1) {
            for (extraBallRemoved in extraBalls downTo 1) {
                newInnings.undoLastAction()
                extraBallsSoFar--
                assertBallsLimitOver(newInnings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
            }
            for (ballRemoved in balls downTo 1) {
                newInnings.undoLastAction()
                ballsSoFar = if (ballsSoFar > 0) ballsSoFar-1 else 5
                oversSoFar = if (ballsSoFar >= 5) oversSoFar-1 else oversSoFar
                assertBallsLimitOver(newInnings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
            }
            newInnings.undoLastAction()
            oversSoFar--
            ballsSoFar = (balls % 6)
            extraBallsSoFar = extraBalls
            if (overRemoved > 1) {
                assertBallsLimitOver(
                    newInnings,
                    ballsSoFar + extraBallsSoFar,
                    6 + extraBallsSoFar,
                    oversSoFar
                )
            }
        }
        assertBallsLimitOver(newInnings, 0, 6, 0)
    }

    @Test
    fun  testGivenActionCombinationWithUndoWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val innings = Innings.newInnings()
        val balls = 9
        var ballsSoFar = 0
        val extraBalls = 3
        var extraBallsSoFar = 0
        val overs = 5
        var oversSoFar = 0
        for (over in 1..overs) {
            for (overInner in 1..2) {
                innings.endOver()
            }
            innings.undoLastAction()
            oversSoFar++
            ballsSoFar = 0
            extraBallsSoFar = 0
            for (balls in 1..balls) {
                innings.ballBowledAndEndOverCheck()
                ballsSoFar++
            }
            innings.undoLastAction()
            ballsSoFar--
            oversSoFar += ballsSoFar / 6
            ballsSoFar %= 6
            for (extraBalls in 1..extraBalls) {
                innings.extraBall()
                extraBallsSoFar++
            }
            innings.undoLastAction()
            extraBallsSoFar--
        }
        assertBallsLimitOver(innings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.fromFile(appContext)
        assertBallsLimitOver(newInnings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
    }

    @Test
    fun testGivenActionsWhenNewInningsThenNewInnings() {
        val innings = Innings.newInnings()
        val balls = 9
        var ballsSoFar = 0
        val extraBalls = 3
        var extraBallsSoFar = 0
        val overs = 5
        var oversSoFar = 0
        for (over in 1..overs) {
            innings.endOver()
            oversSoFar++
            ballsSoFar = 0
            extraBallsSoFar = 0
            for (balls in 1..balls) {
                innings.ballBowledAndEndOverCheck()
                ballsSoFar++
            }
            oversSoFar += ballsSoFar / 6
            ballsSoFar %= 6
            for (extraBalls in 1..extraBalls) {
                innings.extraBall()
                extraBallsSoFar++
            }
        }
        assertBallsLimitOver(innings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
        innings.writeToInningsFile(appContext)
        val newInnings = Innings.newInnings()
        assertBallsLimitOver(newInnings, 0, 6, 0)
    }


}