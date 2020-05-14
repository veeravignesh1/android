package com.toggl.timer.startedit.util

fun String.lastSubstringFromAnyTokenToPosition(tokens: CharArray, position: Int = this.length): Pair<Char?, String> {
    val endIndex = position.coerceIn(0, this.length)
    return substring(0, endIndex)
        .lastIndexOfAny(tokens)
        .let { index ->
            if (index == -1) null to this
            else this.elementAt(index) to this.substring(index + 1, endIndex)
        }
}