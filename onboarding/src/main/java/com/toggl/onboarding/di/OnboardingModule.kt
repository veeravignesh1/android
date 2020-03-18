package com.toggl.onboarding.di

import com.toggl.api.login.LoginApi
import com.toggl.api.login.MockLoginApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = [OnboardingComponent::class])
class OnboardingModule {
    @Provides
    @Singleton
    fun loginApi(): LoginApi =
        MockLoginApi()
}
