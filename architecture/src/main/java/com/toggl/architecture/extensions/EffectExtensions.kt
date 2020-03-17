package com.toggl.architecture.extensions

import com.toggl.architecture.core.Effect

fun <T, R> Effect<T>.map(mapFn: (T?) -> R?): Effect<R> =
    MapEffect(this, mapFn)

private class MapEffect<T, R>(
    private val innerEffect: Effect<T>,
    private val mapFn: (T?) -> R?
) : Effect<R> {
    override suspend fun execute(): R? =
        innerEffect.execute()?.run(mapFn)
}

fun <Action> List<Effect<Action>>.compose(): Effect<Action> =
    CompositeEffect(this)

private class CompositeEffect<Action>(val effects: List<Effect<Action>>) : Effect<Action> {
    override suspend fun execute(): Action? =
        effects.fold<Effect<Action>, Action?>(null) { action, effect ->
            action ?: effect.execute()
        }
}

fun <T> noEffect(): Effect<T> =
    NoEffect()

private class NoEffect<Action> : Effect<Action> {
    override suspend fun execute(): Action? = null

    override fun equals(other: Any?): Boolean =
        other is NoEffect<*>
}
