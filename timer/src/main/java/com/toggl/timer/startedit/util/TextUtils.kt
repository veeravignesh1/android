package com.toggl.timer.startedit.util

fun String.lastSubstringFromTokenToPosition(token: Char, position: Int = this.length): Pair<Char?, String> =
    this.lastSubstringFromAnyTokenToPosition(charArrayOf(token), position)

fun String.lastSubstringFromAnyTokenToPosition(tokens: CharArray, position: Int = this.length): Pair<Char?, String> {
    return this.findLastIndexOfSubstringFromAnyTokenToPosition(tokens, position)
        .let { (startIndex, endIndex) ->
            if (startIndex == -1) null to this
            else this.elementAt(startIndex) to this.substring(startIndex + 1, endIndex)
        }
}

private fun String.findLastIndexOfSubstringFromAnyTokenToPosition(tokens: CharArray, position: Int = this.length): Pair<Int, Int> {
    val endIndex = position.coerceIn(0, this.length)
    return substring(0, endIndex)
        .lastIndexOfAny(tokens)
        .let { index -> index to endIndex }
}