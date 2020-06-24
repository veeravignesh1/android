package com.toggl.settings.di

import com.toggl.settings.ui.SettingsFragment
import dagger.Subcomponent

@Subcomponent
interface SettingsComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsComponent
    }

    fun inject(fragment: SettingsFragment)
}

interface SettingsComponentProvider {
    fun provideSettingsComponent(): SettingsComponent
}