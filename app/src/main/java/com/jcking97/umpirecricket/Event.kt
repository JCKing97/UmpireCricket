package com.jcking97.umpirecricket

import org.json.JSONObject
import java.io.Serializable

class Event(val eventType: EventType, val causedByPreviousEvent: Boolean = false): Serializable {

    companion object {

        private const val eventTypeKey = "eventType"
        private const val causedByPreviousEventKey = "causedByPreviousEvent"

        fun fromJson(jsonObject: JSONObject): Event {
            return Event(
                EventType.valueOf(jsonObject.getString(eventTypeKey)),
                jsonObject.getBoolean(causedByPreviousEventKey)
            )
        }

    }

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