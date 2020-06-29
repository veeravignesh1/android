package com.toggl.settings.di

import com.toggl.settings.ui.EditSettingsFragment
import com.toggl.settings.ui.SettingsFragment
import dagger.Subcomponent

@Subcomponent
interface SettingsComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsComponent
    }

    fun inject(fragment: SettingsFragment)
    fun inject(fragment: EditSettingsFragment)
}

interface SettingsComponentProvider {
    fun provideSettingsComponent(): SettingsComponent
}