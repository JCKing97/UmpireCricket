package com.jcking97.umpirecricket

import org.json.JSONObject
import java.io.*
import java.util.*

/**
 * An innings where one team bats and one team bowls.
 * In our case this consists of a number of overs.
 *
 * @property bowlers The bowlers.
 * @property oversBowled The number of overs bowled so far in the innings.
 * @property overs A list of overs bowled in order that they have been bowled.
 * @property eventStack A stack of all the events e.g. balls and overs bowled that have happened
 */
class Innings constructor(
    var bowlers: ArrayList<Bowler> = arrayListOf(Bowler(0)),
    private var oversBowled: Int = 0,
    val overs: MutableList<Over> = mutableListOf(Over(bowlers[0])),
    private val eventStack: LinkedList<Event> = LinkedList<Event>()
): Serializable {

    /**
     * End the current over and create a new one.
     */
    fun endOver() {
        oversBowled++
        overs.add(Over())
    }

    /**
     * Undo the last end over action.
     */
    fun undoEndOver() {
        if (oversBowled > 0) {
            oversBowled--
            overs.removeAt(overs.lastIndex)
        }
    }

    /**
     * @return the number of overs bowled so far this innings.
     */
    fun getOversBowled(): Int {
        return oversBowled
    }

    /**
     * @return the current over
     */
    fun getCurrentOver(): Over {
        return overs[oversBowled]
    }

    fun newBowler(bowler: Bowler) {
        bowlers.add(bowler)
    }

    fun undoAddBowler(bowler: Bowler) {
        bowlers.remove(bowler)
    }

}