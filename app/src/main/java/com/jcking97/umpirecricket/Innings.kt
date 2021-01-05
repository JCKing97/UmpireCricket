package com.jcking97.umpirecricket

import java.io.Serializable

class Innings(
    private var currentOver: Int = 0,
    private val overs: MutableList<Over> = mutableListOf(Over())): Serializable {

    companion object {

        fun fromFile(): Innings {
            return Innings()
        }

        fun newInnings(): Innings {
            return Innings()
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