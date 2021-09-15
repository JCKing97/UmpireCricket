package com.jcking97.umpirecricket

import java.io.Serializable

class Bowlers(val bowlers: List<Bowler> = listOf(Bowler("Bowler 1")), val currentBowler: Int? = 0): Serializable {

    operator fun iterator(): Iterator<Bowler> {
        return bowlers.iterator()
    }

}