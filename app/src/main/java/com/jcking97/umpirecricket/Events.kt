package com.jcking97.umpirecricket

import org.json.JSONArray
import java.io.Serializable
import java.util.*
import kotlin.NoSuchElementException

/**
 * A serializable collection of events.
 */
class Events: Serializable {

    /**
     * The underlying collection of events.
     */
    private val events =  LinkedList<Event>()

    /**
     * @return A JSONArray representation of the events.
     */
    fun toJson(): JSONArray {
        val json = JSONArray()
        events.forEach {
            json.put(it.toJson())
        }
        return json
    }

    /**
     * Undo the event at the top of the stack and any events that caused it.
     */
    fun undoLastEventAndCausingEvents(): Boolean {
        var triggeredSelectBowlerActivity = false
        return try {
            do {
                val event = events.pop()
                if (EventType.valueOf(event.eventType) == EventType.OVER_BOWLED) {
                    triggeredSelectBowlerActivity = true
                }
                val causedByPreviousEvent = event.doEventInverse()
            } while (causedByPreviousEvent)
            triggeredSelectBowlerActivity
        } catch (e: NoSuchElementException) {
            triggeredSelectBowlerActivity
        }
    }

    /**
     * Execute the given event and any subsequent events, adding them to the stack.
     *
     * @param event The original event to execute
     */
    fun executeEvent(event: Event): Boolean {
        var triggerBowlerActivity = false
        var currentEvent: Event? = event
        do {
            events.push(currentEvent!!)
            if (EventType.valueOf(currentEvent.eventType) == EventType.OVER_BOWLED) {
                triggerBowlerActivity = true
            }
            currentEvent = currentEvent.doEvent()
        } while (currentEvent != null)
        return triggerBowlerActivity
    }

    /**
     * Execute the given event but do not execute any subsequent events the given event causes.
     *
     * @param event The event to execute
     */
    fun executeEventButNotEventsItCauses(event: Event) {
        if (event.eventType == EventType.OVER_BOWLED.toString() && !event.causedByPreviousEvent) {
            println("Over bowled not caused by previous event")
        }
        events.push(event)
        event.doEvent()
    }

    /**
     * @return true if events are empty, false if not.
     */
    fun isEmpty(): Boolean {
        return events.isEmpty()
    }

}