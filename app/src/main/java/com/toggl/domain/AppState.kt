package com.toggl.domain

import com.toggl.architecture.Loadable
import com.toggl.calendar.common.domain.CalendarState
import com.toggl.common.feature.navigation.BackStack
import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.models.domain.Client
import com.toggl.models.domain.DateFormat
import com.toggl.models.domain.DurationFormat
import com.toggl.models.domain.Project
import com.toggl.models.domain.Tag
import com.toggl.models.domain.Task
import com.toggl.models.domain.TimeEntry
import com.toggl.models.domain.User
import com.toggl.models.domain.UserPreferences
import com.toggl.models.domain.Workspace
import com.toggl.onboarding.domain.states.OnboardingState
import com.toggl.timer.common.domain.TimerState
import java.time.DayOfWeek

data class AppState(
    val user: Loadable<User> = Loadable.Uninitialized,
    val userPreferences: UserPreferences = UserPreferences(
        isManualModeEnabled = false,
        is24HourClock = false,
        selectedWorkspaceId = 1,
        dateFormat = DateFormat.DDMMYYYY_dash,
        durationFormat = DurationFormat.Improved,
        firstDayOfTheWeek = DayOfWeek.MONDAY,
        shouldGroupSimilarTimeEntries = true,
        hasCellSwipeActions = true
    ),
    val feedbackMessage: String = "",
    val workspaces: Map<Long, Workspace> = mapOf(),
    val projects: Map<Long, Project> = mapOf(),
    val tasks: Map<Long, Task> = mapOf(),
    val clients: Map<Long, Client> = mapOf(),
    val tags: Map<Long, Tag> = mapOf(),
    val timeEntries: Map<Long, TimeEntry> = mapOf(),
    val backStack: BackStack = emptyList(),
    val calendarPermissionWasGranted: Boolean = false,
    val calendarEvents: Map<String, CalendarEvent> = mapOf(),
    val onboardingLocalState: OnboardingState.LocalState = OnboardingState.LocalState(),
    val timerLocalState: TimerState.LocalState = TimerState.LocalState(),
    val calendarLocalState: CalendarState.LocalState = CalendarState.LocalState()
)
