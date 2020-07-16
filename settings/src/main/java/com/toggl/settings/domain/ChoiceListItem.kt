package com.toggl.settings.domain

data class ChoiceListItem(
    val label: String = "",
    val isSelected: Boolean = false,
    val selectedAction: SettingsAction? = null
) {
    fun dispatchSelected(dispatcher: (SettingsAction) -> Unit = {}) {
        selectedAction?.run(dispatcher)
    }
}