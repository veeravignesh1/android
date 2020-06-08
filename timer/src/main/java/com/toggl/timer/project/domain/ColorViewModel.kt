package com.toggl.timer.project.domain

sealed class ColorViewModel {
    data class DefaultColor(val color: String, val selected: Boolean) : ColorViewModel()
    data class CustomColor(val selected: Boolean) : ColorViewModel()
    object PremiumLocked : ColorViewModel()
}