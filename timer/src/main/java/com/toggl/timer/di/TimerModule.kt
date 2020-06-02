package com.toggl.timer.di

import android.content.Context
import com.toggl.architecture.core.Store
import com.toggl.architecture.core.combine
import com.toggl.architecture.core.decorateWith
import com.toggl.architecture.core.optionalPullback
import com.toggl.architecture.core.pullback
import com.toggl.common.feature.handleClosableActionsUsing
import com.toggl.common.feature.timeentry.TimeEntryAction
import com.toggl.common.feature.timeentry.TimeEntryReducer
import com.toggl.common.feature.timeentry.TimeEntryState
import com.toggl.environment.services.time.TimeService
import com.toggl.timer.R
import com.toggl.timer.common.domain.TimerAction
import com.toggl.timer.common.domain.TimerReducer
import com.toggl.timer.common.domain.TimerState
import com.toggl.timer.common.domain.isProjectCloseAction
import com.toggl.timer.common.domain.isStartEditCloseAction
import com.toggl.timer.common.domain.setEditableProjectToNull
import com.toggl.timer.common.domain.setEditableTimeEntryToNull
import com.toggl.timer.log.domain.TimeEntriesLogAction
import com.toggl.timer.log.domain.TimeEntriesLogReducer
import com.toggl.timer.log.domain.TimeEntriesLogSelector
import com.toggl.timer.log.domain.TimeEntriesLogState
import com.toggl.timer.project.domain.ProjectAction
import com.toggl.timer.project.domain.ProjectReducer
import com.toggl.timer.project.domain.ProjectState
import com.toggl.timer.running.domain.RunningTimeEntryAction
import com.toggl.timer.running.domain.RunningTimeEntryReducer
import com.toggl.timer.running.domain.RunningTimeEntryState
import com.toggl.timer.startedit.domain.ProjectTagChipSelector
import com.toggl.timer.startedit.domain.StartEditAction
import com.toggl.timer.startedit.domain.StartEditReducer
import com.toggl.timer.startedit.domain.StartEditState
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@Module(subcomponents = [TimerComponent::class])
class TimerModule {

    @Provides
    internal fun timeEntriesLogSelector(
        context: Context,
        timeService: TimeService
    ): TimeEntriesLogSelector {
        val todayString = context.getString(R.string.today)
        val yesterdayString = context.getString(R.string.yesterday)

        return TimeEntriesLogSelector(
            todayString,
            yesterdayString,
            timeService
        )
    }

    @Provides
    internal fun projectTagChipSelector(context: Context) =
        ProjectTagChipSelector(
            context.getString(R.string.add_project),
            context.getString(R.string.add_tags)
        )

    @ExperimentalCoroutinesApi
    @Provides
    internal fun timeEntriesLogStore(store: Store<TimerState, TimerAction>): Store<TimeEntriesLogState, TimeEntriesLogAction> =
        store.view(
            mapToLocalState = TimeEntriesLogState.Companion::fromTimerState,
            mapToGlobalAction = TimeEntriesLogAction.Companion::toTimerAction
        )

    @ExperimentalCoroutinesApi
    @Provides
    internal fun startTimeEntryStore(store: Store<TimerState, TimerAction>): Store<StartEditState, StartEditAction> =
        store.optionalView(
            mapToLocalState = StartEditState.Companion::fromTimerState,
            mapToGlobalAction = StartEditAction.Companion::toTimerAction
        )

    @ExperimentalCoroutinesApi
    @Provides
    internal fun runningTimeEntryStore(store: Store<TimerState, TimerAction>): Store<RunningTimeEntryState, RunningTimeEntryAction> =
        store.view(
            mapToLocalState = RunningTimeEntryState.Companion::fromTimerState,
            mapToGlobalAction = RunningTimeEntryAction.Companion::toTimerAction
        )

    @ExperimentalCoroutinesApi
    @Provides
    internal fun projectStore(store: Store<TimerState, TimerAction>): Store<ProjectState, ProjectAction> =
        store.optionalView(
            mapToLocalState = ProjectState.Companion::fromTimerState,
            mapToGlobalAction = ProjectAction.Companion::toTimerAction
        )

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    @Provides
    @Singleton
    internal fun timerReducer(
        timeEntryReducer: TimeEntryReducer,
        timeEntriesLogReducer: TimeEntriesLogReducer,
        startEditReducer: StartEditReducer,
        runningTimeEntryReducer: RunningTimeEntryReducer,
        projectReducer: ProjectReducer
    ): TimerReducer {

        return combine(
            timeEntriesLogReducer.decorateWith(timeEntryReducer).pullback(
                mapToLocalState = TimeEntriesLogState.Companion::fromTimerState,
                mapToLocalAction = TimeEntriesLogAction.Companion::fromTimerAction,
                mapToGlobalState = TimeEntriesLogState.Companion::toTimerState,
                mapToGlobalAction = TimeEntriesLogAction.Companion::toTimerAction
            ),
            startEditReducer.decorateWith(timeEntryReducer).optionalPullback(
                mapToLocalState = StartEditState.Companion::fromTimerState,
                mapToLocalAction = StartEditAction.Companion::fromTimerAction,
                mapToGlobalState = StartEditState.Companion::toTimerState,
                mapToGlobalAction = StartEditAction.Companion::toTimerAction
            ),
            runningTimeEntryReducer.decorateWith(timeEntryReducer).pullback(
                mapToLocalState = RunningTimeEntryState.Companion::fromTimerState,
                mapToLocalAction = RunningTimeEntryAction.Companion::fromTimerAction,
                mapToGlobalState = RunningTimeEntryState.Companion::toTimerState,
                mapToGlobalAction = RunningTimeEntryAction.Companion::toTimerAction
            ),
            projectReducer.optionalPullback(
                mapToLocalState = ProjectState.Companion::fromTimerState,
                mapToLocalAction = ProjectAction.Companion::fromTimerAction,
                mapToGlobalState = ProjectState.Companion::toTimerState,
                mapToGlobalAction = ProjectAction.Companion::toTimerAction
            ),
            handleClosableActionsUsing(TimerAction::isStartEditCloseAction, TimerState::setEditableTimeEntryToNull),
            handleClosableActionsUsing(TimerAction::isProjectCloseAction, TimerState::setEditableProjectToNull)
        )
    }

    private fun TimeEntriesLogReducer.decorateWith(timeEntryReducer: TimeEntryReducer)  =
        this.decorateWith(
            timeEntryReducer,
            mapToLocalState = { TimeEntryState(it.timeEntries) },
            mapToLocalAction = { TimeEntryAction.fromTimeEntryActionHolder(it) },
            mapToGlobalState = { globalState, localState -> globalState.copy(timeEntries = localState.timeEntries) },
            mapToGlobalAction = { localAction -> TimeEntriesLogAction.TimeEntryHandling(localAction) }
        )

    private fun StartEditReducer.decorateWith(timeEntryReducer: TimeEntryReducer) =
        this.decorateWith(
            timeEntryReducer,
            mapToLocalState = { TimeEntryState(it.timeEntries) },
            mapToLocalAction = { TimeEntryAction.fromTimeEntryActionHolder(it) },
            mapToGlobalState = { globalState, localState -> globalState.copy(timeEntries = localState.timeEntries) },
            mapToGlobalAction = { localAction -> StartEditAction.TimeEntryHandling(localAction) }
        )

    private fun RunningTimeEntryReducer.decorateWith(timeEntryReducer: TimeEntryReducer) =
        this.decorateWith(
            timeEntryReducer,
            mapToLocalState = { TimeEntryState(it.timeEntries) },
            mapToLocalAction = { TimeEntryAction.fromTimeEntryActionHolder(it) },
            mapToGlobalState = { globalState, localState -> globalState.copy(timeEntries = localState.timeEntries) },
            mapToGlobalAction = { localAction -> RunningTimeEntryAction.TimeEntryHandling(localAction) }
        )
}