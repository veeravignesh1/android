package com.toggl.timer.log.ui

import com.airbnb.epoxy.EpoxyController
import com.toggl.timer.log.domain.DayHeaderViewModel
import com.toggl.timer.log.domain.FlatTimeEntryViewModel
import com.toggl.timer.log.domain.TimeEntriesLogAction
import com.toggl.timer.log.domain.TimeEntryGroupViewModel
import com.toggl.timer.log.domain.TimeEntryViewModel

class TimeEntryController(val store: TimeEntriesLogStoreViewModel) : EpoxyController() {

    var items: List<TimeEntryViewModel> = listOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    var continueSwiped: Long = 0

    init {
        isDebugLoggingEnabled = true
    }

    override fun buildModels() {
        for (i in items) {
            when (i) {
                is DayHeaderViewModel -> timeEntryHeader {
                    id(i.dayTitle)
                    text(i.dayTitle)
                    duration(i.totalDuration)
                }
                is FlatTimeEntryViewModel -> timeEntryItem {
                    id(i.id)
                    timeEntry(i)
                    onContinueTappedListener { store.dispatch(TimeEntriesLogAction.ContinueButtonTapped(it)) }
                    onTappedListener { store.dispatch(TimeEntriesLogAction.TimeEntryTapped(it)) }
                    isSwiped(i.id == continueSwiped)
                }
                is TimeEntryGroupViewModel -> timeEntryGroup {
                    id(i.groupId)
                    timeEntryGroup(i)
                    onContinueTappedListener { store.dispatch(TimeEntriesLogAction.ContinueButtonTapped(it)) }
                    onTappedListener { store.dispatch(TimeEntriesLogAction.TimeEntryGroupTapped(it)) }
                    onExpandTappedListener { store.dispatch(TimeEntriesLogAction.ToggleTimeEntryGroupTapped(it)) }
                    isSwiped(i.groupId == continueSwiped)
                }
            }
        }
    }
}