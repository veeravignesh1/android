package com.toggl.common.feature.extensions

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.extensions.noEffect

fun <T, Action> MutableValue<T>.mutateWithoutEffects(mutateFn: T.() -> T): List<Effect<Action>> {
    this.mutate(mutateFn)
    return noEffect()
}

infix fun <T, Action> T.returnEffect(effects: List<Effect<Action>>) = effects

fun <T> T.withoutEffects() = noEffect()