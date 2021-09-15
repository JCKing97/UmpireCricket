package com.jcking97.umpirecricket

/**
 * The types of possible events in an innings e.g. ball bowled, over bowled etc.
 */
enum class EventType {
    BALL_BOWLED,
    EXTRA_BALL,
    OVER_BOWLED,
    NEW_BOWLER,
    CHANGE_BOWLER_NAME,
    SELECT_BOWLER
}