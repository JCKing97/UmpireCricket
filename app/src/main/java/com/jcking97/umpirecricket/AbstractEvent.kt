package com.jcking97.umpirecricket

import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.util.*

/**
 * An abstract event that occurred in an innings.
 *
 * @param causedByPreviousEvent Whether the event was cause by the event directly prior to it
 */
abstract class Event(val innings: Innings, val causedByPreviousEvent: Boolean = false): Serializable {

    abstract val eventType: String

    /**
     * Do the event, executing actions on the innings.
     *
     * @return An event caused by this event.
     */
    abstract fun doEvent(): Event?

    /**
     * Do the inverse of the event, undoing any actions on the innings.
     *
     * @return true if event has been caused by another.
     */
    abstract fun doEventInverse(): Boolean

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
        fun fromJson(innings: Innings, json: JSONObject): Event {
            val eventType = json.getString(eventTypeKey)
            return try {
                when (EventType.valueOf(eventType)) {
                    EventType.BALL_BOWLED -> BallBowledEvent(innings)
                    EventType.EXTRA_BALL -> ExtraBallEvent(innings)
                    EventType.OVER_BOWLED -> EndOverEvent(innings, json.getBoolean(causedByPreviousEventKey))
                    EventType.NEW_BOWLER -> NewBowlerEvent(innings)
                }
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
        jsonObject.put(eventTypeKey, eventType)
        jsonObject.put(causedByPreviousEventKey, causedByPreviousEvent)
        return jsonObject
    }
}
