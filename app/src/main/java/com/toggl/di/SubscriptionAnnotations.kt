package com.toggl.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ProvideAppSubscription

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ProvideLoadTimeEntriesSubscription

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ProvideProjectsSubscription