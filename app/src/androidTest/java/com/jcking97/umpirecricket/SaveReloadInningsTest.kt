package com.jcking97.umpirecricket

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.File

import com.jcking97.umpirecricket.Utilities.assertBallsLimitOver


/**
 * Test the Innings' classes save and reload from file feature.
 */
@RunWith(AndroidJUnit4::class)
class SaveReloadInningsTest {

    private lateinit var appContext: Context
    private val filename = "test.json"
    private val newInningsLoader = NewInningsLoader()
    private lateinit var loadFile: File
    private lateinit var fileInningsLoader: FileInningsLoader
    private lateinit var inningsFileWriter: InningsFileWriter

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // Remove all saved files
        appContext.filesDir.list { dir, name -> File(dir, name).delete() }
        loadFile = File(appContext.filesDir, filename)
        fileInningsLoader = FileInningsLoader(loadFile)
        inningsFileWriter = InningsFileWriter(loadFile)
    }

    @Test
    fun testGivenNoPreviousSaveWhenReloadFromFileThenActionsPreserved() {
        val inningsAndEvents = fileInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        Utilities.assertBallsLimitOver(innings, 0, 6, 0)
    }

    @Test
    fun testGivenBallBowledWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        inningsAndEvents.events.executeEvent(BallBowledEvent(inningsAndEvents.innings))
        assertBallsLimitOver(inningsAndEvents.innings, 1, 6, 0)
        inningsFileWriter.writeEvents(inningsAndEvents.events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        assertBallsLimitOver(newInningsAndEvents.innings, 1, 6, 0)
    }

    @Test
    fun testGivenExtraBallWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val inningsandEvents = newInningsLoader.loadInnings()
        val innings = inningsandEvents.innings
        val events = inningsandEvents.events
        events.executeEvent(ExtraBallEvent(innings))
        assertBallsLimitOver(innings, 1, 7, 0)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        assertBallsLimitOver(newInningsAndEvents.innings, 1, 7, 0)
    }

    @Test
    fun testGivenEndOverWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        events.executeEvent(EndOverEvent(innings, false))
        assertBallsLimitOver(innings, 0, 6, 1)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        assertBallsLimitOver(newInningsAndEvents.innings, 0, 6, 1)
    }

    @Test
    fun testGivenBallBowledWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenActionsRemoved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        events.executeEvent(BallBowledEvent(innings))
        assertBallsLimitOver(innings, 1, 6, 0)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        assertBallsLimitOver(newInningsAndEvents.innings, 1, 6, 0)
        newInningsAndEvents.events.undoLastEventAndCausingEvents()
        assertBallsLimitOver(newInningsAndEvents.innings, 0, 6, 0)
    }

    @Test
    fun testGivenExtraBallWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenActionsRemoved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        events.executeEvent(ExtraBallEvent(innings))
        assertBallsLimitOver(innings, 1, 7, 0)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        assertBallsLimitOver(newInningsAndEvents.innings, 1, 7, 0)
        newInningsAndEvents.events.undoLastEventAndCausingEvents()
        assertBallsLimitOver(newInningsAndEvents.innings, 0, 6, 0)
    }

    @Test
    fun testGivenEndOverWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenActionsRemoved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        events.executeEvent(EndOverEvent(innings, false))
        assertBallsLimitOver(innings, 0, 6, 1)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        assertBallsLimitOver(newInningsAndEvents.innings, 0, 6, 1)
        newInningsAndEvents.events.undoLastEventAndCausingEvents()
        assertBallsLimitOver(newInningsAndEvents.innings, 0, 6, 0)
    }

    @Test
    fun testGivenBallsBowledAndOneUndoWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        for (ball in 1..2) {
            events.executeEvent(BallBowledEvent(innings))
        }
        events.undoLastEventAndCausingEvents()
        assertBallsLimitOver(innings, 1, 6, 0)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        assertBallsLimitOver(newInningsAndEvents.innings, 1, 6, 0)
    }

    @Test
    fun testGivenExtraBallsAndOneUndoWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        for (extraBall in 1..2) {
            events.executeEvent(ExtraBallEvent(innings))
        }
        events.undoLastEventAndCausingEvents()
        assertBallsLimitOver(innings, 1, 7, 0)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        assertBallsLimitOver(newInningsAndEvents.innings, 1, 7, 0)
    }

    @Test
    fun testGivenEndOverTwiceAndUndoOnceWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        for (over in 1..2) {
            events.executeEvent(EndOverEvent(innings, false))
        }
        events.undoLastEventAndCausingEvents()
        assertBallsLimitOver(innings, 0, 6, 1)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        assertBallsLimitOver(newInningsAndEvents.innings, 0, 6, 1)
    }

    @Test
    fun testGivenBallsBowledAndOneUndoWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenActionsRemoved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        for (ball in 1..2) {
            events.executeEvent(BallBowledEvent(innings))
        }
        assertBallsLimitOver(innings, 2, 6, 0)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        val newInnings = newInningsAndEvents.innings
        val newEvents = newInningsAndEvents.events
        assertBallsLimitOver(newInnings, 2, 6, 0)
        newEvents.undoLastEventAndCausingEvents()
        assertBallsLimitOver(newInnings, 1, 6, 0)
    }

    @Test
    fun testGivenExtraBallsAndOneUndoWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenActionsRemoved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        for (extraBall in 1..2) {
            events.executeEvent(ExtraBallEvent(innings))
        }
        assertBallsLimitOver(innings, 2, 8, 0)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        val newInnings = newInningsAndEvents.innings
        val newEvents = newInningsAndEvents.events
        assertBallsLimitOver(newInnings, 2, 8, 0)
        newEvents.undoLastEventAndCausingEvents()
        assertBallsLimitOver(newInnings, 1, 7, 0)
    }

    @Test
    fun testGivenEndOverTwiceAndUndoOnceWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenActionsRemoved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        for (over in 1..2) {
            events.executeEvent(EndOverEvent(innings, false))
        }
        assertBallsLimitOver(innings, 0, 6, 2)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        val newInnings = newInningsAndEvents.innings
        val newEvents = newInningsAndEvents.events
        assertBallsLimitOver(newInnings, 0, 6, 2)
        newEvents.undoLastEventAndCausingEvents()
        assertBallsLimitOver(newInnings, 0, 6, 1)
    }

    @Test
    fun  testGivenActionCombinationWhenSaveToAndReloadFromFileThenActionsPreserved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        val balls = 9
        var ballsSoFar = 0
        val extraBalls = 3
        var extraBallsSoFar = 0
        val overs = 5
        var oversSoFar = 0
        for (over in 1..overs) {
            events.executeEvent(EndOverEvent(innings, false))
            oversSoFar++
            ballsSoFar = 0
            extraBallsSoFar = 0
            for (balls in 1..balls) {
                events.executeEvent(BallBowledEvent(innings))
                ballsSoFar++
            }
            oversSoFar += ballsSoFar / 6
            ballsSoFar %= 6
            for (extraBalls in 1..extraBalls) {
                events.executeEvent(ExtraBallEvent(innings))
                extraBallsSoFar++
            }
        }
        assertBallsLimitOver(innings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        assertBallsLimitOver(newInningsAndEvents.innings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
    }

    @Test
    fun  testGivenActionCombinationWhenSaveToAndReloadFromFileThenActionsPreservedWhenUndoThenRemoved() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        val balls = 9
        var ballsSoFar = 0
        val extraBalls = 3
        var extraBallsSoFar = 0
        val overs = 5
        var oversSoFar = 0
        for (over in 1..overs) {
            events.executeEvent(EndOverEvent(innings, false))
            oversSoFar++
            ballsSoFar = 0
            extraBallsSoFar = 0
            for (ball in 1..balls) {
                events.executeEvent(BallBowledEvent(innings))
                ballsSoFar++
            }
            oversSoFar += ballsSoFar / 6
            ballsSoFar %= 6
            for (extraBall in 1..extraBalls) {
                events.executeEvent(ExtraBallEvent(innings))
                extraBallsSoFar++
            }
        }
        assertBallsLimitOver(innings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        val newInnings = newInningsAndEvents.innings
        val newEvents = newInningsAndEvents.events
        assertBallsLimitOver(newInnings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
        for (overRemoved in overs downTo 1) {
            for (extraBallRemoved in extraBalls downTo 1) {
                newEvents.undoLastEventAndCausingEvents()
                extraBallsSoFar--
                assertBallsLimitOver(newInnings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
            }
            for (ballRemoved in balls downTo 1) {
                newEvents.undoLastEventAndCausingEvents()
                ballsSoFar = if (ballsSoFar > 0) ballsSoFar-1 else 5
                oversSoFar = if (ballsSoFar >= 5) oversSoFar-1 else oversSoFar
                assertBallsLimitOver(newInnings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
            }
            newEvents.undoLastEventAndCausingEvents()
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
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        val balls = 9
        var ballsSoFar = 0
        val extraBalls = 3
        var extraBallsSoFar = 0
        val overs = 5
        var oversSoFar = 0
        for (over in 1..overs) {
            for (overInner in 1..2) {
                events.executeEvent(EndOverEvent(innings, false))
            }
            events.undoLastEventAndCausingEvents()
            oversSoFar++
            ballsSoFar = 0
            extraBallsSoFar = 0
            for (balls in 1..balls) {
                events.executeEvent(BallBowledEvent(innings))
                ballsSoFar++
            }
            events.undoLastEventAndCausingEvents()
            ballsSoFar--
            oversSoFar += ballsSoFar / 6
            ballsSoFar %= 6
            for (extraBalls in 1..extraBalls) {
                events.executeEvent(ExtraBallEvent(innings))
                extraBallsSoFar++
            }
            events.undoLastEventAndCausingEvents()
            extraBallsSoFar--
        }
        assertBallsLimitOver(innings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = fileInningsLoader.loadInnings()
        assertBallsLimitOver(newInningsAndEvents.innings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
    }

    @Test
    fun testGivenActionsWhenNewInningsThenNewInnings() {
        val inningsAndEvents = newInningsLoader.loadInnings()
        val innings = inningsAndEvents.innings
        val events = inningsAndEvents.events
        val balls = 9
        var ballsSoFar = 0
        val extraBalls = 3
        var extraBallsSoFar = 0
        val overs = 5
        var oversSoFar = 0
        for (over in 1..overs) {
            events.executeEvent(EndOverEvent(innings, false))
            oversSoFar++
            ballsSoFar = 0
            extraBallsSoFar = 0
            for (balls in 1..balls) {
                events.executeEvent(BallBowledEvent(innings))
                ballsSoFar++
            }
            oversSoFar += ballsSoFar / 6
            ballsSoFar %= 6
            for (extraBalls in 1..extraBalls) {
                events.executeEvent(ExtraBallEvent(innings))
                extraBallsSoFar++
            }
        }
        assertBallsLimitOver(innings, ballsSoFar+extraBallsSoFar, 6+extraBallsSoFar, oversSoFar)
        inningsFileWriter.writeEvents(events)
        val newInningsAndEvents = newInningsLoader.loadInnings()
        assertBallsLimitOver(newInningsAndEvents.innings, 0, 6, 0)
    }


}