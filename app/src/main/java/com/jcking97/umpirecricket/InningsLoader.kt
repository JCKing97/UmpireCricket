package com.jcking97.umpirecricket

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.Serializable

/**
 * A serializable interface that guarantees any implementor must be able to load an innings.
 */
interface InningsLoader: Serializable {

    /**
     * Load an innings and it's events.
     */
    fun loadInnings(): InningsAndEvents

}

/**
 * An innings loader that loads the innings from the given file.
 *
 * @param file The file to load the innings from.
 */
class FileInningsLoader(private val file: File): InningsLoader {

    /**
     * Load the innings and it's events from file.
     */
    override fun loadInnings(): InningsAndEvents {
        val innings = Innings()
        val events = try {
            createEventsFromFile(innings)
        } catch (e: FileNotFoundException) {
            Log.e(
                "Load events failure",
                "File: ${file.absolutePath} not found when trying to load events"
            )
            Events()
        } catch (e: JSONException) {
            Log.e(
                "Load events failure",
                "Error with json: ${e.message}"
            )
            Events()
        }
        return InningsAndEvents(innings, events)
    }

    /**
     * Read the file and create the events it represents.
     *
     * @param innings The innings the events belong to.
     * @return The events represented in the file.
     */
    @Throws(FileNotFoundException::class, JSONException::class)
    private fun createEventsFromFile(innings: Innings): Events {
        val events = Events()
        FileReader(file).use {
            val json = JSONArray(it.readText())
            println("EVENTS LOADING")
            for (i in json.length() - 1 downTo 0) {
                val event = Event.fromJson(innings, json.getJSONObject(i))
                events.executeEventButNotEventsItCauses(event)
            }
        }
        return events
    }

}

/**
 * An innings loader that loads a new innings and events.
 */
class NewInningsLoader: InningsLoader {

    /**
     * Load a new innings and events.
     */
    override fun loadInnings(): InningsAndEvents {
        val innings = Innings()
        val events = Events()
        return InningsAndEvents(innings, events)
    }

}