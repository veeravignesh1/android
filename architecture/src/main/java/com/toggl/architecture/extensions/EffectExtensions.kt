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

fun <Action> Collection<Action>.toEffects() = map { it.toEffect() }

fun <Action> Action.toEffect(): Effect<Action> = object : Effect<Action> {
    override suspend fun execute(): Action? = this@toEffect
}

fun <Action> effect(effect: Effect<Action>) = listOf(effect)

fun <Action> effectOf(effect: Action) = listOf(effect.toEffect())

fun <Action> effects(vararg effects: Effect<Action>) = effects.toList()

fun <Action> effects(vararg actions: Action) = actions.map { it.toEffect() }.toList()

infix operator fun <Action> Effect<Action>.plus(otherEffect: Effect<Action>) = listOf(this, otherEffect)

fun noEffect(): List<Effect<Nothing>> =
    emptyList()
