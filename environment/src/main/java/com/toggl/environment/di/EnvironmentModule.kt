package com.toggl.environment.di

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo.FLAG_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.toggl.api.ApiClient
import com.toggl.api.ApiService
import com.toggl.api.AuthInterceptor
import com.toggl.api.FeedbackBody
import com.toggl.api.feedback.FeedbackApi
import com.toggl.api.serializers.FeedbackBodySerializer
import com.toggl.environment.ext.AppBuildConfig
import com.toggl.environment.services.analytics.AnalyticsService
import com.toggl.environment.services.analytics.AppCenterAnalyticsService
import com.toggl.environment.services.analytics.CompositeAnalyticsService
import com.toggl.environment.services.analytics.FirebaseAnalyticsService
import com.toggl.environment.services.calendar.CalendarService
import com.toggl.environment.services.calendar.CursorCalendarService
import com.toggl.environment.services.permissions.LollipopPermissionRequesterService
import com.toggl.environment.services.permissions.MarshmallowPermissionRequesterService
import com.toggl.environment.services.permissions.PermissionCheckerService
import com.toggl.environment.services.permissions.PermissionRequesterService
import com.toggl.environment.services.time.JavaTimeService
import com.toggl.environment.services.time.TimeService
import com.toggl.models.domain.InstallLocation
import com.toggl.models.domain.PlatformInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.TimeZone
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object EnvironmentModule {
    @Provides
    @Singleton
    fun timeService(): TimeService = JavaTimeService()

    @Provides
    @Singleton
    fun analyticsService(
        firebaseAnalyticsService: FirebaseAnalyticsService,
        appCenterAnalyticsService: AppCenterAnalyticsService
    ): AnalyticsService = CompositeAnalyticsService(firebaseAnalyticsService, appCenterAnalyticsService)

    @Provides
    @Singleton
    fun calendarService(@ApplicationContext context: Context): CalendarService = CursorCalendarService(context)

    @Provides
    @Singleton
    fun permissionCheckerService(@ApplicationContext context: Context): PermissionCheckerService =
        object : PermissionCheckerService {
            override fun hasCalendarPermission(): Boolean {
                val calendarPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR)
                return calendarPermission == PackageManager.PERMISSION_GRANTED
            }
        }

    @Provides
    fun platformInfo(@ApplicationContext context: Context): PlatformInfo {
        val language = Locale.getDefault().displayLanguage
        val timezone = TimeZone.getDefault()
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionName = packageInfo.versionName

        @Suppress("DEPRECATION")
        val build = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) packageInfo.versionCode.toString()
        else packageInfo.longVersionCode.toString()
        val model = Build.MODEL
        val osVersion = Build.VERSION.RELEASE
        val installLocation = when {
            packageInfo.applicationInfo == null -> InstallLocation.Unknown
            packageInfo.applicationInfo.flags.and(FLAG_EXTERNAL_STORAGE) == FLAG_EXTERNAL_STORAGE -> InstallLocation.External
            else -> InstallLocation.Internal
        }
        return PlatformInfo(
            language,
            timezone.id,
            versionName,
            build,
            model,
            osVersion,
            installLocation
        )
    }

    @Provides
    @ApiAuthCredentials
    fun authCredentials(): String {
        // TODO: actually get the current logged in user api token
        val validApiToken = "api token"
        val authStringBytes = validApiToken.toByteArray(charset = Charsets.UTF_8)
        return Base64.encodeToString(authStringBytes, Base64.DEFAULT)
    }

    @Provides
    fun authInterceptor(@ApiAuthCredentials authString: String?): AuthInterceptor = AuthInterceptor(authString)

    @Provides
    @BaseApiUrl
    fun baseEndpointUrl(): String =
        if (AppBuildConfig.isBuildTypeRelease) "https://mobile.toggl.com"
        else "https://mobile.toggl.space"

    @Provides
    @LoggedInOkHttpClient
    fun okHttpClient(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    @Provides
    fun providesGson(): Gson = GsonBuilder()
        .registerTypeAdapter(FeedbackBody::class.java, FeedbackBodySerializer())
        .create()

    @Provides
    fun providesApiService(
        @BaseApiUrl baseUrl: String,
        @LoggedInOkHttpClient okHttpClient: OkHttpClient,
        gson: Gson
    ): ApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)

    @Provides
    @Singleton
    fun apiClient(apiService: ApiService): ApiClient = ApiClient(apiService)

    @Provides
    @Singleton
    fun feedbackApi(apiClient: ApiClient): FeedbackApi = apiClient
}

@Module
@InstallIn(ActivityComponent::class)
object EnvironmentActivityModule {

    @Provides
    fun permissionRequesterService(activity: Activity): PermissionRequesterService =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) LollipopPermissionRequesterService()
        else MarshmallowPermissionRequesterService(activity as AppCompatActivity)
}
