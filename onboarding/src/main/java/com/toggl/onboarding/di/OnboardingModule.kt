package com.toggl.onboarding.di

import com.toggl.api.login.LoginApi
import com.toggl.api.login.MockLoginApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object OnboardingModule {
    @Provides
    @Singleton
    fun loginApi(): LoginApi =
        MockLoginApi()
}
