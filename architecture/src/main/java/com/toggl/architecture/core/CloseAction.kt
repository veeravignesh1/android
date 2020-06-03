package com.toggl.architecture.core

interface CloseAction

fun Any?.isCloseAction(): Boolean =
    when (this) {
        is CloseAction -> true
        is ActionWrapper<*> -> this.action?.isCloseAction() ?: false
        else -> false
    }


