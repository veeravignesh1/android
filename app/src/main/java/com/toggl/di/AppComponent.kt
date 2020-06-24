package com.toggl.di

import com.toggl.TogglApplication
import com.toggl.architecture.StoreScopeProvider
import com.toggl.calendar.di.CalendarComponent
import com.toggl.calendar.di.CalendarModule
import com.toggl.database.DatabaseModule
import com.toggl.environment.di.EnvironmentModule
import com.toggl.onboarding.di.OnboardingComponent
import com.toggl.onboarding.di.OnboardingModule
import com.toggl.repository.di.RepositoryModule
import com.toggl.settings.di.SettingsComponent
import com.toggl.settings.di.SettingsModule
import com.toggl.timer.di.TimerComponent
import com.toggl.timer.di.TimerModule
import com.toggl.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ReducerModule::class,
        ViewModelModule::class,
        OnboardingModule::class,
        TimerModule::class,
        CalendarModule::class,
        SettingsModule::class,
        EnvironmentModule::class,
        DatabaseModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: TogglApplication,
            @BindsInstance storeScopeProvider: StoreScopeProvider
        ): AppComponent
    }

    fun inject(activity: MainActivity)

    fun inject(togglApplication: TogglApplication)

    @Singleton
    fun onboardingComponent(): OnboardingComponent.Factory

    @Singleton
    fun timerComponent(): TimerComponent.Factory

    @Singleton
    fun calendarComponent(): CalendarComponent.Factory

    @Singleton
    fun settingsComponent(): SettingsComponent.Factory
}
