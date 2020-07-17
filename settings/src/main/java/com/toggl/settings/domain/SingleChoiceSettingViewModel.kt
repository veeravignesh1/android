package com.toggl.settings.domain

data class SingleChoiceSettingViewModel(
    val header: String,
    val items: List<ChoiceListItem>
) {
    companion object {
        val Empty = SingleChoiceSettingViewModel("", listOf())
    }
}
