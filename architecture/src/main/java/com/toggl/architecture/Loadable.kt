package com.toggl.architecture

sealed class Loadable<out Value> {
    open operator fun invoke(): Value? = null

    object Uninitialized : Loadable<Nothing>()
    object Loading : Loadable<Nothing>()
    data class Error<out Value>(val failure: Failure) : Loadable<Value>()
    data class Loaded<out Value>(val value: Value) : Loadable<Value>() {
        override operator fun invoke(): Value = value
    }
}