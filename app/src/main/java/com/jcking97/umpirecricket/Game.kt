package com.jcking97.umpirecricket

import java.io.Serializable

class Game(
    var currentOver: Int = 0,
    val overs: MutableList<Over> = mutableListOf(Over())): Serializable {

    companion object {

        fun fromFile(): Game {
            return Game()
        }

        fun newGame(): Game {
            return Game()
        }

    }

    fun ballBowled() {
        overs[currentOver].ballBowled()
        if (overs[currentOver].currentBall >= overs[currentOver].ballLimit) {
            endOver()
        }
    }

    fun extraBall() {
        overs[currentOver].extraBall()
    }

    fun endOver() {
        currentOver++
        newOver()
    }

    fun newOver() {
        overs.add(Over())
    }

    fun getCurrentBall(): Int {
        return overs[currentOver].currentBall
    }

    fun getBallLimit(): Int {
        return overs[currentOver].ballLimit
    }

    fun getOverCount(): Int {
        return currentOver
    }

}