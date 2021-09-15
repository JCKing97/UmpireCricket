package com.jcking97.umpirecricket

import java.io.Serializable

class Bowler(
    var name: String = "Bowler",
    var oversBowled: Int = 0): Serializable {

    fun overBowled() {
        oversBowled++
    }

    fun changeName(newName: String) {
        name = newName
    }

}