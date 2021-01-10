package com.jcking97.umpirecricket

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.Serializable

/**
 * Handle writing of innings data and events to file.
 */
class InningsFileWriter(private val file: File): Serializable {

    /**
     * Write events as a json list to file.
     */
    fun writeEvents(events: Events) {
        try {
            val json = events.toJson()
            writeJsonToFile(json)
        } catch (e: JSONException) {
            Log.e(
                "Innings to file failure",
                "Failed to write innings to file (JSONException): ${e.message}"
            )
        }
    }

    /**
     * Write the given JSONArray to the given File.
     *
     * @param json The JSONArray to write to file.
     */
    private fun writeJsonToFile(json: JSONArray) {
        FileWriter(file).use {
            try {
                it.write(json.toString())
            } catch (e: IOException) {
                Log.e("innings.writeToFile", "Failed to write to file", e)
            }
        }
    }

}