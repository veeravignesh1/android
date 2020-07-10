package com.toggl.calendar.datepicker.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.toggl.calendar.R
import com.toggl.calendar.datepicker.domain.CalendarDatePickerAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CalendarDatePickerFragment : Fragment(R.layout.fragment_calendardatepicker) {

    private val store: CalendarDatePickerStoreViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        store.dispatch(CalendarDatePickerAction.OnViewAppeared)

        store.state
            .onEach { _ -> }
            .launchIn(lifecycleScope)
    }
}
