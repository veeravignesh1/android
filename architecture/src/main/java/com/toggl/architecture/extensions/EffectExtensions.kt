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

fun <Action> effect(effect: Effect<Action>) = listOf(effect)

fun <Action> effects(vararg effects: Effect<Action>) = listOf(effects)

infix operator fun <Action> Effect<Action>.plus(otherEffect: Effect<Action>) = listOf(this, otherEffect)

fun <T> noEffect(): List<Effect<T>> =
    emptyList()