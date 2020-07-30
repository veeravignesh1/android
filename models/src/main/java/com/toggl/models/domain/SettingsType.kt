package com.toggl.models.domain

sealed class SettingsType {
    open class SingleChoiceSetting : SettingsType()

    object Name : SettingsType()
    object Email : SettingsType()
    object Workspace : SingleChoiceSetting()
    object DateFormat : SingleChoiceSetting()
    object TwentyFourHourClock : SettingsType()
    object DurationFormat : SingleChoiceSetting()
    object FirstDayOfTheWeek : SingleChoiceSetting()
    object GroupSimilar : SettingsType()
    object CellSwipe : SettingsType()
    object ManualMode : SettingsType()
    object CalendarSettings : SettingsType()
    object AllowCalendarAccess : SettingsType()
    data class Calendar(val id: String) : SettingsType()
    object SmartAlert : SingleChoiceSetting()
    object SubmitFeedback : SettingsType()
    object About : SettingsType()
    object PrivacyPolicy : SettingsType()
    object TermsOfService : SettingsType()
    object Licenses : SettingsType()
    object Help : SettingsType()
    object SignOut : SettingsType()
}
