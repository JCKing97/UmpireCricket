package com.jcking97.umpirecricket

import org.junit.Test
import org.junit.Before

import org.junit.Assert.*
import java.lang.Integer.min

/**
 * Tests for the Over class.
 */
class GameTest {

    private lateinit var game: Game

    @Before
    fun setUp() {
        game = Game.newGame()
    }

    @Test
    fun whenNewGameThenOversSetAndBallsSetAndLimitSet() {
        assertEquals(6, game.getBallLimit())
        assertEquals(0, game.getCurrentBall())
        assertEquals(0, game.getOverCount())
    }

}