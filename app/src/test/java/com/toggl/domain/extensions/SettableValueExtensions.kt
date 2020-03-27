package com.toggl.domain.extensions

import com.toggl.architecture.core.SettableValue

fun <T> T.toSettableValue(setFunction: (T) -> Unit) =
    SettableValue({ this }, setFunction)