package com.toggl.models.domain

data class PlatformInfo(
    val currentNativeLanguageCode: String,
    val timezoneIdentifier: String,
    val version: String,
    val buildNumber: String,
    val phoneModel: String,
    val osVersion: String,
    val installLocation: InstallLocation
)

enum class InstallLocation {
    External, Internal, Unknown
}