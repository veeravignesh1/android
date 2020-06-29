package com.toggl.di

import com.toggl.initializers.AppCenterInitializer
import com.toggl.initializers.AppInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppInitializerModule {
    @Binds
    @IntoSet
    abstract fun provideAppCenterInitializer(bind: AppCenterInitializer): AppInitializer
}
