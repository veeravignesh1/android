package com.toggl.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.toggl.common.feature.navigation.Route
import com.toggl.settings.domain.SettingsAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take

@AndroidEntryPoint
class BrowserFragment : Fragment() {
    private val store: SettingsStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        store.state
            .take(1)
            .onEach {
                when (val route = it.backStack.last()) {
                    is Route.Browser -> open(route.parameter)
                    else -> close()
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        close()
    }

    private fun open(uri: Uri) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = uri
        startActivity(openURL)
    }

    private fun close() {
        store.dispatch(SettingsAction.CloseBrowser)
    }
}