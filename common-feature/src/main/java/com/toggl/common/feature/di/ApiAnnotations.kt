package com.toggl.common.feature.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class BaseApiUrl

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ApiAuthCredentials

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class LoggedInOkHttpClient