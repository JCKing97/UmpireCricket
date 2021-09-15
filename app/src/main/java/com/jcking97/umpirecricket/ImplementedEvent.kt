package com.jcking97.umpirecricket

/**
 * An event where the ball was bowled.
 *
 * @param innings The innings where this ball was bowled
 */
class BallBowledEvent(innings: Innings): Event(innings) {

    override val eventType = EventType.BALL_BOWLED.toString()

    /**
     * Increment the amount of balls bowled in the over.
     * If this ball that has been bowled has caused the over to end, return an EndOverEvent,
     * else return null.
     *
     * @return An EndOver Event if this event caused it, else null.
     */
    override fun doEvent(): Event? {
        val currentOver = innings.getCurrentOver()
        currentOver.ballBowled()
        val isOverEnded = currentOver.isBallLimitMetOrExceeded()
        return if (isOverEnded) {
            EndOverEvent(innings, causedByPreviousEvent = true)
        } else {
            null
        }
    }

    /**
     * Decrement the current overs amount of balls bowled.
     *
     * @return false as this event cannot be caused by another.
     */
    override fun doEventInverse(): Boolean {
        val currentOver = innings.getCurrentOver()
        currentOver.undoBallBowled()
        return false
    }
}

/**
 * An event where a ball has been bowled causing an extra ball in the over.
 *
 * @param innings The innings where this ball was bowled
 */
class ExtraBallEvent(innings: Innings): Event(innings) {

    override val eventType = EventType.EXTRA_BALL.toString()

    /**
     * Incrememnt the ball limit and the amount of balls bowled in the current over.
     *
     * @return null as this event never causes another event
     */
    override fun doEvent(): Event? {
        val currentOver = innings.getCurrentOver()
        currentOver.extraBall()
        return null
    }

    /**
     * Decrement the amount of balls bowled and the ball limit of the current over.
     *
     * @return false as this event is never caused by a preceding event.
     */
    override fun doEventInverse(): Boolean {
        val currentOver = innings.getCurrentOver()
        currentOver.undoExtraBall()
        return false
    }

}

/**
 * An event where the over has ended.
 *
 * @param innings The innings where this over has ended.
 */
class EndOverEvent(innings: Innings, causedByPreviousEvent: Boolean): Event(innings, causedByPreviousEvent) {

    override val eventType = EventType.OVER_BOWLED.toString()

    /**
     * End the current over of the innings.
     *
     * @return null as this event never causes another.
     */
    override fun doEvent(): Event? {
        innings.endOver()
        return null
    }

    /**
     * Undo the end of the last over.
     *
     * @return true if caused by a ball being bowled else return false.
     */
    override fun doEventInverse(): Boolean {
        innings.undoEndOver()
        return causedByPreviousEvent
    }

}

/**
 * An event where a new bowler has been created.
 *
 * @param innings The innings where this over has ended.
 */
class NewBowlerEvent(innings: Innings): Event(innings) {

    override val eventType = EventType.NEW_BOWLER.toString()
    lateinit var bowler: Bowler

    /**
     * Add the new bowler to the innings.
     *
     * @return null as this event never causes another.
     */
    override fun doEvent(): Event? {
        bowler = Bowler()
        innings.newBowler(bowler)
        return null
    }

    /**
     * Undo adding the new bowler to the innings.
     *
     * @return false as this event is never caused by another.
     */
    override fun doEventInverse(): Boolean {
        innings.undoAddBowler(bowler)
        return false
    }

}

///**
// * An event where a new bowler has been created.
// *
// * @param innings The innings where this over has ended.
// */
//class ChangeBowlerNameEvent(innings: Innings): Event(innings) {
//
//    override val eventType = EventType.CHANGE_BOWLER_NAME.toString()
//    lateinit var bowler: Bowler
//
//    /**
//     * Add the new bowler to the innings.
//     *
//     * @return null as this event never causes another.
//     */
//    override fun doEvent(): Event? {
//        bowler = Bowler()
//        innings.newBowler(bowler)
//        return null
//    }
//
//    /**
//     * Undo adding the new bowler to the innings.
//     *
//     * @return false as this event is never caused by another.
//     */
//    override fun doEventInverse(): Boolean {
//        innings.undoAddBowler(bowler)
//        return false
//    }
//
//}
