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
    fun undoLastEventAndCausingEvents() {
        return try {
            do {
                val event = events.pop()
                val causedByPreviousEvent = event.doEventInverse()
            } while (causedByPreviousEvent)
        } catch (e: NoSuchElementException) {
            // No events to undo
        }
    }

    /**
     * Execute the given event and any subsequent events, adding them to the stack.
     *
     * @param event The original event to execute
     */
    fun executeEvent(event: Event) {
        var currentEvent: Event? = event
        do {
            events.push(currentEvent!!)
            currentEvent = currentEvent.doEvent()
        } while (currentEvent != null)
    }

    /**
     * Execute the given event but do not execute any subsequent events the given event causes.
     *
     * @param event The event to execute
     */
    fun executeEventButNotEventsItCauses(event: Event) {
        if (event.eventType == EventType.OVER_BOWLED.toString() && !event.causedByPreviousEvent) {
            println("Over bowled not caused by previoud event")
        }
        events.push(event)
        event.doEvent()
    }

}