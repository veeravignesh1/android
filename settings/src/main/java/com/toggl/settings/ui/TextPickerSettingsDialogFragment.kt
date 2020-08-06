package com.toggl.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.toggl.common.feature.navigation.getRouteParam
import com.toggl.models.domain.SettingsType
import com.toggl.settings.compose.extensions.createComposeFullscreenView
import com.toggl.settings.ui.common.TextPickerDialogWithHeader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class TextPickerSettingsDialogFragment : DialogFragment() {
    private val store: SettingsStoreViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = createComposeFullscreenView {

        TextPickerDialogWithHeader(
            setting = store.state.map { it.backStack.getRouteParam<SettingsType.TextSetting>() ?: throw IllegalStateException() },
            user = store.state.map { it.user },
            dispatcher = store::dispatch
        )
    }
}