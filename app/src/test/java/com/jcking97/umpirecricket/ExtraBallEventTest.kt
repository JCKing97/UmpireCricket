package com.jcking97.umpirecricket

import com.jcking97.umpirecricket.Utilities.assertBallsLimitOver
import org.junit.Before
import org.junit.Test

class ExtraBallEventTest {

    private lateinit var innings: Innings
    private lateinit var extraBallEvent: ExtraBallEvent

    @Before
    fun setUp() {
        innings = Innings()
        extraBallEvent = ExtraBallEvent(innings)
    }

    @Test
    fun whenExtraBallsThenBallLimitIncreasedAndCurrentBallIncreasedAndOverNotIncreased() {
        for (ball in 1..12) {
            extraBallEvent.doEvent()
            assertBallsLimitOver(innings, ball, ball+6, 0)
        }
    }

    @Test
    fun whenExtraBallThenAddedWhenUndoThenRemoved(){
        for (ball in 1..5) {
            extraBallEvent.doEvent()
            assertBallsLimitOver(innings, 1, 7, 0)
            extraBallEvent.doEventInverse()
            assertBallsLimitOver(innings, 0, 6, 0)
        }
    }

    @Test
    fun whenMultipleExtraBallsAddedAndUndoThenRemoved(){
        for (ball in 1..5) {
            extraBallEvent.doEvent()
            assertBallsLimitOver(innings, ball, 6+ball, 0)
        }
        for (ball in 4 downTo 0) {
            extraBallEvent.doEventInverse()
            assertBallsLimitOver(innings, ball, 6+ball, 0)
        }
    }

}