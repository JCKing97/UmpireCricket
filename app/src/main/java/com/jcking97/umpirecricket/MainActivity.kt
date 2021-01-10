package com.jcking97.umpirecricket

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.File

/**
 * The activity that the user starts off with when they open the app.
 */
class MainActivity : AppCompatActivity() {

    /**
     * The name of the file in the files directory to read and write innings data from/to.
     */
    private val filename = "innings.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up new and continue innings buttons
        val newInningsActivity = findViewById<Button>(R.id.newInningsButton)
        newInningsActivity.setOnClickListener{ newInnings() }

        val continueInningsActivity = findViewById<Button>(R.id.continueInningsButton)
        continueInningsActivity.setOnClickListener{ continueInnings() }
    }

    /**
     * Start a new innings activity.
     */
    private fun newInnings() {
        val intent = getInningsIntent()
        val newInningsLoader = NewInningsLoader()
        intent.putExtra("inningsLoader", newInningsLoader)
        putInningsFileWriter(intent)
        startActivity(intent)
    }

    /**
     * Load the innings and events from file and start the innings activity.
     */
    private fun continueInnings() {
        val intent = getInningsIntent()
        val file = File(applicationContext.filesDir, filename)
        val fileInningsLoader = FileInningsLoader(file)
        intent.putExtra("inningsLoader", fileInningsLoader)
        putInningsFileWriter(intent)
        startActivity(intent)
    }

    /**
     * Put an InningsFileWriter in the given intent.
     *
     * @param intent The intent to put the InningsFileWriter in
     */
    private fun putInningsFileWriter(intent: Intent) {
        val file = getInningsFile()
        val inningsFileWriter = InningsFileWriter(file)
        intent.putExtra("inningsFileWriter", inningsFileWriter)
    }

    /**
     * Get the innings file to load from and write to.
     *
     * @return The innings file
     */
    private fun getInningsFile(): File {
        return File(applicationContext.filesDir, filename)
    }

    /**
     * Get an intent to move to an innings.
     */
    private fun getInningsIntent(): Intent {
        return Intent(this, InningsActivity::class.java)
    }

}