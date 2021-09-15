package com.jcking97.umpirecricket

import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

class Bowler(
    var id: Int,
    var name: String = "Bowler",
    var oversBowled: Int = 0): Serializable {

    companion object {
        const val idKey = "id"
        const val nameKey = "name"
        const val oversBowledKey = "oversBowled"

        /**
         * Create a Bowler from a JSONObject.
         *
         * @param json The json to create the bowler from
         * @return A bowler object that was represented by the JSON.
         * @throws JSONException If the json is not valid for the Event
         */
        @Throws(JSONException::class)
        fun findBowlerFromJSON(innings: Innings, json: JSONObject): Bowler? {
            val bowlerToFindID = json.getInt(idKey)
            for (bowler in innings.bowlers) {
                if (bowlerToFindID == bowler.id) {
                    return bowler
                }
            }
            return null
        }
    }

    fun overBowled() {
        oversBowled++
    }

    fun changeName(newName: String) {
        name = newName
    }

    override fun equals(other: Any?): Boolean {
        return other is Bowler && other.id == id
    }

    /**
     * Convert the Bowler to json format.
     *
     * @return The JSONObject representing the bowler
     * @throws JSONException If fails to create the json object
     */
    @Throws(JSONException::class)
    open fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put(idKey, id)
        jsonObject.put(nameKey, name)
        jsonObject.put(oversBowledKey, oversBowled)
        return jsonObject
    }

}