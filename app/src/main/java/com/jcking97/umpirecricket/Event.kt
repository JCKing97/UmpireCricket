package com.jcking97.umpirecricket

import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.lang.IllegalArgumentException

class Event(val eventType: EventType, val causedByPreviousEvent: Boolean = false): Serializable {

    companion object {

        private const val eventTypeKey = "eventType"
        private const val causedByPreviousEventKey = "causedByPreviousEvent"

        @Throws(JSONException::class)
        fun fromJson(jsonObject: JSONObject): Event {
            val eventType = jsonObject.getString(eventTypeKey)
            try {
                return Event(
                    EventType.valueOf(eventType),
                    jsonObject.getBoolean(causedByPreviousEventKey)
                )
            } catch (e: IllegalArgumentException) {
                throw JSONException(
                    "Incorrect event type: ${eventType}, available: ${EventType.values()}"
                )
            }
        }

    }

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