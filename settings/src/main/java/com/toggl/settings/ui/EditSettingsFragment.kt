package com.toggl.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.Recomposer
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.ui.core.setContent
import com.toggl.common.feature.navigation.Route
import com.toggl.common.feature.navigation.getRouteParam
import com.toggl.models.domain.DateFormat
import com.toggl.models.domain.DurationFormat
import com.toggl.models.domain.SettingsType
import com.toggl.models.domain.UserPreferences
import com.toggl.settings.R
import com.toggl.settings.domain.ChoiceListItem
import com.toggl.settings.domain.SettingsAction
import com.toggl.settings.domain.getTranslatedRepresentation
import com.toggl.settings.domain.toPresentableList
import com.toggl.settings.ui.composables.ChoiceListWithHeader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import java.time.DayOfWeek

@AndroidEntryPoint
class EditSettingsFragment : DialogFragment() {
    private val store: SettingsStoreViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FrameLayout(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        store.state
            .mapNotNull {
                if (it.backStack.last() is Route.SettingsEdit) {
                    return@mapNotNull Pair(it.userPreferences, it.backStack.getRouteParam<SettingsType>()!!)
                } else {
                    null
                }
            }
            .onEach { displayChoiceList(it.first, it.second) }
            .launchIn(lifecycleScope)
    }

    private fun displayChoiceList(userPreferences: UserPreferences, settingsType: SettingsType) {
        val (settingHeader, settingChoiceListItems) = settingsType.toPresentableList(requireContext(), userPreferences)

        (view as ViewGroup).setContent(Recomposer.current()) {
            ChoiceListWithHeader(
                items = listOf(settingChoiceListItems).asFlow(),
                header = settingHeader,
                dispatcher = store::dispatch
            )
        }
    }
}