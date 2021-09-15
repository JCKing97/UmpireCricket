package com.jcking97.umpirecricket

import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import java.io.Serializable

data class Bowler(var name: String): Serializable

class BowlerNameEditText(appContext: Context, private val bowler: Bowler): AppCompatEditText(appContext) {

    init {
        
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        println(bowler == null)
        bowler.name = text.toString()
    }

    fun initialise() {
        setText(bowler.name, BufferType.EDITABLE)
    }

}
