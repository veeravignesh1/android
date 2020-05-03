package com.toggl.timer.startedit.ui.editduration

import android.text.Spanned

class WheelDurationInputFilter(
    val onDigitEntered: (digit: Int) -> Unit,
    val onDeletionDetected: () -> Unit
) : android.text.InputFilter {

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence {
        val empty = String()
        val sourceLength = source.length

        if (sourceLength > 1)
            return source

        if (sourceLength == 0) {
            onDeletionDetected()
            return "0"
        }

        val lastChar = source.last()

        if (lastChar.isDigit()) {
            val digit = Character.getNumericValue(lastChar)
            onDigitEntered(digit)

            return empty
        }

        return empty
    }
}