package com.toggl.domain

import com.toggl.architecture.Loadable
import com.toggl.calendar.common.domain.CalendarState
import com.toggl.models.domain.UserPreferences
import com.toggl.models.domain.Client
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.Project
import com.toggl.models.domain.Tag
import com.toggl.models.domain.Task
import com.toggl.models.domain.TimeEntry
import com.toggl.models.domain.User
import com.toggl.models.domain.Workspace
import com.toggl.onboarding.domain.states.OnboardingState
import com.toggl.timer.common.domain.TimerState

data class AppState(
    val user: Loadable<User> = Loadable.Uninitialized,
    val userPreferences: UserPreferences = UserPreferences(
        false
    ),
    val workspaces: Map<Long, Workspace> = mapOf(),
    val projects: Map<Long, Project> = mapOf(),
    val tasks: Map<Long, Task> = mapOf(),
    val clients: Map<Long, Client> = mapOf(),
    val tags: Map<Long, Tag> = mapOf(),
    val timeEntries: Map<Long, TimeEntry> = mapOf(),
    val editableTimeEntry: EditableTimeEntry? = null,
    val calendarPermissionWasGranted: Boolean = false,
    val onboardingLocalState: OnboardingState.LocalState = OnboardingState.LocalState(),
    val timerLocalState: TimerState.LocalState = TimerState.LocalState(),
    val calendarLocalState: CalendarState.LocalState = CalendarState.LocalState()
)
