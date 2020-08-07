package com.toggl.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.toggl.architecture.extensions.select
import com.toggl.common.feature.compose.extensions.createComposeFullscreenView
import com.toggl.settings.domain.SingleChoiceSettingSelector
import com.toggl.settings.ui.common.SingleChoiceDialogWithHeader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class SingleChoiceSettingsDialogFragment : DialogFragment() {
    @Inject
    @JvmField
    var singleChoiceSettingSelector: SingleChoiceSettingSelector? = null // https://github.com/google/dagger/issues/1883#issuecomment-642565920 🤷‍
    private val store: SettingsStoreViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = createComposeFullscreenView {
        val selectedSingleChoiceState = store.state.select(singleChoiceSettingSelector!!)

        SingleChoiceDialogWithHeader(selectedSingleChoiceState, store::dispatch)
    }
}
