package com.toggl.settings.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.ui.core.setContent
import com.toggl.architecture.extensions.select
import com.toggl.common.services.permissions.PermissionRequesterService
import com.toggl.common.services.permissions.requestCalendarPermissionIfNeeded
import com.toggl.common.feature.compose.extensions.createComposeView
import com.toggl.settings.di.ProvideCalendarSettingsSelector
import com.toggl.settings.domain.SettingsAction
import com.toggl.settings.domain.SettingsSelector
import com.toggl.settings.ui.SettingsStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CalendarSettingsFragment : Fragment() {
    @Inject @JvmField var permissionService: PermissionRequesterService? = null // https://github.com/google/dagger/issues/1883#issuecomment-642565920 🤷‍
    @Inject @JvmField @ProvideCalendarSettingsSelector var calendarSettingsSelector: SettingsSelector? = null // https://github.com/google/dagger/issues/1883#issuecomment-642565920 🤷‍
    private val store: SettingsStoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = createComposeView { statusBarHeight, navigationBarHeight ->
        val selectedState = store.state.select(calendarSettingsSelector!!)
        CalendarSettingsPage(selectedState, statusBarHeight, navigationBarHeight, store::dispatch)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val permissionService = permissionService ?: return

        lifecycleScope.launchWhenResumed {
            permissionService.requestCalendarPermissionIfNeeded()
        }

        store.state
            .map { it.shouldRequestCalendarPermission }
            .filter { shouldRequest -> shouldRequest }
            .distinctUntilChanged()
            .onEach {

                if (permissionService.hasCalendarPermission())
                    return@onEach

                val hasPermission = permissionService.requestCalendarPermission()
                store.dispatch(SettingsAction.CalendarPermissionReceived(hasPermission))
            }
            .launchIn(lifecycleScope)
    }
}
