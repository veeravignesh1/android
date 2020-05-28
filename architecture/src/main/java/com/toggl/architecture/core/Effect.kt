package com.toggl.architecture.core

interface Effect<out Action> {
    suspend fun execute(): Action?
}
