package com.jcking97.umpirecricket

class Event(val eventType: EventType, val causedByPreviousEvent: Boolean = false)

enum class EventType {
    BALL_BOWLED,
    EXTRA_BALL,
    OVER_BOWLED
}