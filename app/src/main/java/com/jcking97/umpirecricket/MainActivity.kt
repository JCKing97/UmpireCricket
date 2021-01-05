package com.jcking97.umpirecricket

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    var innings = Innings()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up new and continue innings buttons
        val newInningsActivity = findViewById<Button>(R.id.newInningsButton)
        newInningsActivity.setOnClickListener{ newInnings() }

        val continueInningsActivity = findViewById<Button>(R.id.continueInningsButton)
        continueInningsActivity.setOnClickListener{ continueInnings() }
    }

    private fun newInnings() {
        val intent = Intent(this, InningsActivity::class.java)
        innings = Innings.newInnings()
        intent.putExtra("innings", innings)
        startActivity(intent)
    }

    private fun continueInnings() {
        val intent = Intent(this, InningsActivity::class.java)
        innings = Innings.fromFile()
        intent.putExtra("innings", innings)
        startActivity(intent)
    }
}