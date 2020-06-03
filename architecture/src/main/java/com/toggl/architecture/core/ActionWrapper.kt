package com.toggl.architecture.core

interface ActionWrapper<WrappedAction> {
    val action: WrappedAction
}

inline fun <From, reified To> From.unwrap(): To? =
    if (this !is ActionWrapper<*> || this.action !is To) null
    else this.action as To

fun <T> Any?.isOrWraps(typeToCheck: Class<T>): Boolean =
    when {
        this == null -> false
        typeToCheck.isAssignableFrom(this::class.java) -> true
        this is ActionWrapper<*> -> this.action.isOrWraps(typeToCheck)
        else -> false
    }