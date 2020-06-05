package com.toggl.timer.di

import com.toggl.timer.log.ui.TimeEntriesLogFragment
import com.toggl.timer.project.ui.ProjectDialogFragment
import com.toggl.timer.running.ui.RunningTimeEntryFragment
import com.toggl.timer.startedit.ui.StartEditDialogFragment
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@Subcomponent
interface TimerComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): TimerComponent
    }

    fun inject(fragment: TimeEntriesLogFragment)
    @FlowPreview
    @ExperimentalCoroutinesApi
    fun inject(fragment: StartEditDialogFragment)
    fun inject(fragment: RunningTimeEntryFragment)
    fun inject(fragment: ProjectDialogFragment)
}

interface TimerComponentProvider {
    fun provideTimerComponent(): TimerComponent
}
