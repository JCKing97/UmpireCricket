package com.jcking97.umpirecricket

import java.io.Serializable

class Over(var currentBall: Int = 0, var ballLimit: Int = 6): Serializable {

    fun ballBowled() {
        if (currentBall < ballLimit) {
            currentBall++
        }
    }

    fun extraBall() {
        ballLimit++
        ballBowled()
    }
}