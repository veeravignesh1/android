package com.toggl.api.di

import com.google.gson.GsonBuilder
import com.toggl.api.clients.ErrorHandlingProxyClient
import com.toggl.api.clients.authentication.AuthenticationApiClient
import com.toggl.api.clients.feedback.FeedbackApiClient
import com.toggl.api.clients.reports.ReportsApiClient
import com.toggl.api.extensions.AppBuildConfig
import com.toggl.api.network.AuthenticationApi
import com.toggl.api.network.FeedbackApi
import com.toggl.api.network.ReportsApi
import com.toggl.api.network.ResetPasswordBody
import com.toggl.api.network.deserializers.TotalsResponseDeserializer
import com.toggl.api.network.deserializers.UserDeserializer
import com.toggl.api.network.interceptors.AuthInterceptor
import com.toggl.api.network.interceptors.UserAgentInterceptor
import com.toggl.api.network.models.feedback.FeedbackBody
import com.toggl.api.network.models.reports.TotalsBody
import com.toggl.api.network.models.reports.TotalsResponse
import com.toggl.api.network.serializers.FeedbackBodySerializer
import com.toggl.api.network.serializers.ResetPasswordBodySerializer
import com.toggl.api.network.serializers.TotalsBodySerializer
import com.toggl.models.domain.User
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApiModule {

    @Provides
    @Singleton
    @BaseUrl
    fun baseEndpointUrl(): String =
        if (AppBuildConfig.isBuildTypeRelease) "https://mobile.track.toggl.com"
        else "https://mobile.track.toggl.space"

    @Provides
    @Singleton
    @BaseApiUrl
    fun baseApiUrl(@BaseUrl baseUrl: String): String = "$baseUrl/api/v9/"

    @Provides
    @Singleton
    @BaseReportsUrl
    fun baseReportsUrl(@BaseUrl baseUrl: String): String = "$baseUrl/reports/api/v3/"

    @Provides
    @Singleton
    fun okHttpClient(
        userAgentInterceptor: UserAgentInterceptor,
        authInterceptor: AuthInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(userAgentInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    @Provides
    @Singleton
    @ApiRetrofit
    fun apiRetrofit(
        @BaseApiUrl baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val converterFactory = GsonBuilder()
            .registerTypeAdapter(ResetPasswordBody::class.java, ResetPasswordBodySerializer())
            .registerTypeAdapter(FeedbackBody::class.java, FeedbackBodySerializer())
            .registerTypeAdapter(User::class.java, UserDeserializer())
            .create()
            .let(GsonConverterFactory::create)

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    @ReportsRetrofit
    fun reportsRetrofit(
        @BaseReportsUrl baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val converterFactory = GsonBuilder()
            .registerTypeAdapter(TotalsBody::class.java, TotalsBodySerializer())
            .registerTypeAdapter(TotalsResponse::class.java, TotalsResponseDeserializer())
            .create()
            .let(GsonConverterFactory::create)

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    internal fun feedbackApi(@ApiRetrofit retrofit: Retrofit) =
        retrofit.create(FeedbackApi::class.java)

    @Provides
    @Singleton
    internal fun authenticationApi(@ApiRetrofit retrofit: Retrofit) =
        retrofit.create(AuthenticationApi::class.java)

    @Provides
    @Singleton
    internal fun reportsApi(@ReportsRetrofit retrofit: Retrofit) =
        retrofit.create(ReportsApi::class.java)
}

@Module
@InstallIn(ApplicationComponent::class)
internal abstract class ApiClientModule {
    @Binds
    abstract fun feedbackApiClient(bind: ErrorHandlingProxyClient): FeedbackApiClient

    @Binds
    abstract fun authenticationApiClient(bind: ErrorHandlingProxyClient): AuthenticationApiClient

    @Binds
    abstract fun reportsApiClient(bind: ErrorHandlingProxyClient): ReportsApiClient
}
