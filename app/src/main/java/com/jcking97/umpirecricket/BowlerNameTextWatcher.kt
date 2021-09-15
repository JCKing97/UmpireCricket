package com.jcking97.umpirecricket

import android.text.Editable
import android.text.TextWatcher

class BowlerNameTextWatcher(val bowler: Bowler, val innings: Innings, val events: Events):
    TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val event = ChangeBowlerNameEvent(innings, bowler, s.toString())
        events.executeEvent(event)
    }

    override fun afterTextChanged(s: Editable?) {
    }


}