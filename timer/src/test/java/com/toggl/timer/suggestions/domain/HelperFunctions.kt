package com.toggl.timer.suggestions.domain

import com.toggl.common.Constants
import com.toggl.environment.services.calendar.CalendarEvent
import com.toggl.models.domain.Client
import com.toggl.models.domain.Project
import com.toggl.models.domain.TimeEntry
import com.toggl.models.domain.User
import com.toggl.models.validation.ApiToken
import com.toggl.models.validation.Email

fun createInitialState(
    user: User = createUser(),
    projects: List<Project> = emptyList(),
    timeEntries: List<TimeEntry> = emptyList(),
    maxNumberOfSuggestions: Int = Constants.Suggestions.maxNumberOfSuggestions,
    suggestions: List<Suggestion> = emptyList(),
    clients: Map<Long, Client> = emptyMap(),
    calendarEvents: Map<String, CalendarEvent> = emptyMap()
) = SuggestionsState(
    user = user,
    projects = projects.associateBy { it.id },
    clients = clients,
    timeEntries = timeEntries.associateBy { it.id },
    maxNumberOfSuggestions = maxNumberOfSuggestions,
    suggestions = suggestions,
    calendarEvents = calendarEvents
)

fun createUser(
    id: Long = 0,
    token: ApiToken = ApiToken.Invalid,
    workspaceId: Long = 10L,
    email: Email.Valid = Email.from("email@email.com") as Email.Valid,
    name: String = ""
) = User(
    id = id,
    apiToken = token,
    defaultWorkspaceId = workspaceId,
    email = email,
    name = name
)