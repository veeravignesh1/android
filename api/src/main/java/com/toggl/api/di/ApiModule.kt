package com.toggl.api.di

import com.google.gson.GsonBuilder
import com.toggl.api.extensions.AppBuildConfig
import com.toggl.api.feedback.FeedbackApiClient
import com.toggl.api.feedback.RetrofitFeedbackApiClient
import com.toggl.api.login.LoginApiClient
import com.toggl.api.login.RetrofitLoginApiClient
import com.toggl.api.network.FeedbackApi
import com.toggl.api.network.FeedbackBody
import com.toggl.api.network.LoginApi
import com.toggl.api.network.interceptors.AuthInterceptor
import com.toggl.api.network.interceptors.UserAgentInterceptor
import com.toggl.api.serializers.FeedbackBodySerializer
import com.toggl.api.serializers.UserDeserializer
import com.toggl.models.domain.User
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
    fun retrofit(
        @BaseApiUrl baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val converterFactory = GsonBuilder()
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
    internal fun feedbackApi(retrofit: Retrofit) =
        retrofit.create(FeedbackApi::class.java)

    @Provides
    @Singleton
    internal fun loginApi(retrofit: Retrofit) =
        retrofit.create(LoginApi::class.java)

    @Provides
    @Singleton
    internal fun feedbackApiClient(feedbackApi: FeedbackApi): FeedbackApiClient =
        RetrofitFeedbackApiClient(feedbackApi)

    @Provides
    @Singleton
    internal fun loginApiClient(loginApi: LoginApi): LoginApiClient =
        RetrofitLoginApiClient(loginApi)
}