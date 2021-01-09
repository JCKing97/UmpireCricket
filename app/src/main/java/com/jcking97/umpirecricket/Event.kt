package com.jcking97.umpirecricket

import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.lang.IllegalArgumentException

/**
 * An event that occurred in an innings.
 *
 * @param eventType The type of the event e.g. ball bowled, over bowled
 * @param causedByPreviousEvent Whether the event was cause by the event directly prior to it
 */
class Event(val eventType: EventType, val causedByPreviousEvent: Boolean = false): Serializable {

    companion object {

        private const val eventTypeKey = "eventType"
        private const val causedByPreviousEventKey = "causedByPreviousEvent"

        /**
         * Create an Event from a JSONObject.
         *
         * @param json The json to create the event from
         * @return An event object that was represented by the JSON.
         * @throws JSONException If the json is not valid for the Event
         */
        @Throws(JSONException::class)
        fun fromJson(json: JSONObject): Event {
            val eventType = json.getString(eventTypeKey)
            try {
                return Event(
                    EventType.valueOf(eventType),
                    json.getBoolean(causedByPreviousEventKey)
                )
            } catch (e: IllegalArgumentException) {
                throw JSONException(
                    "Incorrect event type: ${eventType}, available: ${EventType.values()}"
                )
            }
        }

    }

    /**
     * Convert the Event to json format.
     *
     * @return The JSONObject representing the event
     * @throws JSONException If fails to create the json object
     */
    @Throws(JSONException::class)
    fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put(eventTypeKey, eventType.name)
        jsonObject.put(causedByPreviousEventKey, causedByPreviousEvent)
        return jsonObject
    }
}

enum class EventType {
    BALL_BOWLED,
    EXTRA_BALL,
    OVER_BOWLED
}