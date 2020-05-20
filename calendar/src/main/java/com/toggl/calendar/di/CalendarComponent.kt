package com.toggl.calendar.di

import com.toggl.calendar.ui.CalendarFragment
import dagger.Subcomponent

@Subcomponent
interface CalendarComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): CalendarComponent
    }

    fun inject(fragment: CalendarFragment)
}

interface CalendarComponentProvider {
    fun provideCalendarComponent(): CalendarComponent
}