package com.toggl.common.feature.navigation

interface BackStackAwareState<T> {
    fun popBackStack(): T
}