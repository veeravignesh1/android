package com.toggl.architecture

import kotlinx.coroutines.CoroutineScope

interface StoreScopeProvider {
    fun getStoreScope(): CoroutineScope
}
