package com.toggl.timer.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class MaxNumberOfCalendarSuggestions

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class MaxNumberOfMostUsedSuggestions