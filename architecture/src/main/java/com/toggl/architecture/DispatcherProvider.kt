package com.toggl.architecture

import kotlinx.coroutines.CoroutineDispatcher

data class DispatcherProvider(
    val io: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val main: CoroutineDispatcher
)