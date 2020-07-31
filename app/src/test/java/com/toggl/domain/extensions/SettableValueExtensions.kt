package com.toggl.domain.extensions

import com.toggl.architecture.core.MutableValue

fun <T> T.toMutableValue(setFunction: (T) -> Unit) =
    MutableValue({ this }, setFunction)
