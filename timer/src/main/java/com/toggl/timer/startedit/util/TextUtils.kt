package com.toggl.timer.startedit.util

fun String.findTokenAndQueryMatchesForAutocomplete(token: Char, cursorPosition: Int = this.length): Pair<Char?, String> =
    findTokenAndQueryMatchesForAutocomplete(charArrayOf(token), cursorPosition)

fun String.findTokenAndQueryMatchesForAutocomplete(tokens: CharArray, cursorPosition: Int = this.length): Pair<Char?, String> {
    val endIndex = cursorPosition.coerceIn(0, this.length)

    val joinedTokens = tokens.joinToString("|")

    // This regular expression matches when tokens appear at the beginning of words
    // (IOW, tokens preceded by a space) or if they appear at the beginning of the query
    val regex = "(^| )($joinedTokens)".toRegex()
    val substring = substring(0, endIndex)

    val startIndex = regex.findAll(substring).lastOrNull()?.let {
        substring.indexOfAny(tokens, it.range.first)
    } ?: return null to this

    return elementAt(startIndex) to substring(startIndex + 1)
}
