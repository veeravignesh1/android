package com.toggl.calendar.contextualmenu.ui

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.toggl.architecture.extensions.select
import com.toggl.calendar.R
import com.toggl.calendar.contextualmenu.domain.ContextualMenuAction
import com.toggl.calendar.contextualmenu.domain.ContextualMenuDisplaySelector
import com.toggl.calendar.contextualmenu.domain.ContextualMenuViewModel
import com.toggl.calendar.di.CalendarComponentProvider
import com.toggl.common.feature.domain.formatForDisplay
import kotlinx.android.synthetic.main.fragment_contextualmenu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ContextualMenuFragment : Fragment(R.layout.fragment_contextualmenu) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var contextualMenuDisplaySelector: ContextualMenuDisplaySelector

    private val store: ContextualMenuStoreViewModel by viewModels { viewModelFactory }

    private val contextualMenuAdapter = ContextualMenuActionsAdapter { store.dispatch(it) }

    override fun onAttach(context: Context) {
        (requireActivity().applicationContext as CalendarComponentProvider)
            .provideCalendarComponent().inject(this)

        super.onAttach(context)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actions_recycler_view.adapter = contextualMenuAdapter
        actions_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        store.state
            .select(contextualMenuDisplaySelector)
            .onEach { updateContextualMenu(it) }
            .launchIn(lifecycleScope)

        cancel_button.setOnClickListener {
            store.dispatch(ContextualMenuAction.CloseButtonTapped)
        }
    }

    private fun updateContextualMenu(contextualMenuViewModel: ContextualMenuViewModel) {
        description.text = contextualMenuViewModel.description
        period_label.text = contextualMenuViewModel.periodLabel
        contextualMenuAdapter.submitList(contextualMenuViewModel.contextualMenuActions.actions)

        when (contextualMenuViewModel) {
            is ContextualMenuViewModel.TimeEntryContextualMenu -> updateProjectInfo(contextualMenuViewModel)
            is ContextualMenuViewModel.CalendarEventContextualMenu -> updateCalendarInfo(contextualMenuViewModel)
        }
    }

    private fun updateCalendarInfo(viewModel: ContextualMenuViewModel.CalendarEventContextualMenu) {
        if (viewModel.calendarColor != null && viewModel.calendarName != null) {
            project_dot.visibility = View.VISIBLE
            project_label.visibility = View.VISIBLE
            project_label.text = viewModel.calendarName
            project_dot.setColorFilter(Color.parseColor(viewModel.calendarColor), PorterDuff.Mode.SRC_IN)
        } else {
            project_dot.visibility = View.GONE
            project_label.visibility = View.GONE
        }
    }

    private fun updateProjectInfo(viewModel: ContextualMenuViewModel.TimeEntryContextualMenu) {
        if (viewModel.projectViewModel != null) {
            project_dot.visibility = View.VISIBLE
            project_label.visibility = View.VISIBLE
            project_label.text = viewModel.projectViewModel.formatForDisplay()
            project_dot.setColorFilter(Color.parseColor(viewModel.projectViewModel.color), PorterDuff.Mode.SRC_IN)
        } else {
            project_dot.visibility = View.GONE
            project_label.visibility = View.GONE
        }
    }
}
