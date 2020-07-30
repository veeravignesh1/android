package com.toggl.architecture.core

interface ActionWrapper<WrappedAction> {
    val action: WrappedAction
}

inline fun <From, reified To> From.unwrap(): To? =
    if (this !is ActionWrapper<*> || this.action !is To) null
    else this.action as To

fun Any.asActionSequence(): Sequence<Any> = generateSequence(
    seedFunction = { this },
    nextFunction = {
        if (it is ActionWrapper<*>) it.action
        else null
    }
)

inline fun <reified T> Any?.isOrWraps(): Boolean =
    this?.asActionSequence()?.any { it is T } ?: false
