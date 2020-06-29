package com.toggl

import android.app.Application
import com.toggl.architecture.DispatcherProvider
import com.toggl.architecture.StoreScopeProvider
import com.toggl.initializers.AppInitializers
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltAndroidApp
class TogglApplication : Application(), CoroutineScope, StoreScopeProvider {

    @Inject lateinit var appInitializers: AppInitializers
    @Inject lateinit var dispatchersProviders: DispatcherProvider

    override val coroutineContext: CoroutineContext by lazy {
        dispatchersProviders.main
    }

    override fun onCreate() {
        super.onCreate()
        appInitializers.initialize(this)
    }

    override fun getStoreScope(): CoroutineScope =
        this
}
