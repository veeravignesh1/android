package com.toggl.settings.domain

import arrow.optics.optics
import com.toggl.architecture.Loadable
import com.toggl.common.feature.navigation.BackStack
import com.toggl.models.domain.User
import com.toggl.models.domain.UserPreferences

@optics
data class SettingsState(
    val user: User,
    val userPreferences: UserPreferences,
    val shouldRequestCalendarPermission: Boolean,
    val localState: LocalState,
    val backStack: BackStack
) {
    data class LocalState internal constructor(
        internal val sendFeedbackRequest: Loadable<Unit>
    ) {
        constructor(): this(
            sendFeedbackRequest = Loadable.Uninitialized
        )

        companion object
    }

    companion object
}