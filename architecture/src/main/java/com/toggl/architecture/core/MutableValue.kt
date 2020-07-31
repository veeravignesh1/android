package com.toggl.architecture.core

class MutableValue<T>(private val getValue: () -> T, private val setValue: (T) -> Unit) {

    fun mutate(transformFn: T.() -> (T)) {
        val newValue = transformFn(this())
        setValue(newValue)
    }

    fun <R> mapState(transformFn: T.() -> R): R =
        this().run(transformFn)

    operator fun invoke() = getValue()

    fun <R> map(getMap: (T) -> R, mapSet: (T, R) -> T): MutableValue<R> =
        MutableValue(
            getValue = { getMap(this()) },
            setValue = { value -> this.mutate { mapSet(this, value) } }
        )
}
