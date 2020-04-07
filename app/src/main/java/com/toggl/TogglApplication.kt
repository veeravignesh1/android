package com.toggl

import android.app.Application
import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.StoreScopeProvider
import com.toggl.di.DaggerAppComponent
import com.toggl.initializers.AppInitializers
import com.toggl.onboarding.di.OnboardingComponent
import com.toggl.onboarding.di.OnboardingComponentProvider
import com.toggl.timer.di.TimerComponent
import com.toggl.timer.di.TimerComponentProvider
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TogglApplication : Application(),
    CoroutineScope,
    OnboardingComponentProvider,
    TimerComponentProvider,
    StoreScopeProvider {

    @Inject
    lateinit var appInitializers: AppInitializers

    @Inject
    lateinit var dispatchersProviders: DispatcherProvider

    override val coroutineContext: CoroutineContext by lazy {
        dispatchersProviders.main
    }

    // Reference to the application graph that is used across the whole app
    val appComponent = DaggerAppComponent.factory().create(this, this)

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        appInitializers.initialize(this)
    }

    override fun provideLoginComponent(): OnboardingComponent =
        appComponent.onboardingComponent().create()

    override fun provideTimerComponent(): TimerComponent =
        appComponent.timerComponent().create()

    override fun getStoreScope(): CoroutineScope =
        this
}
