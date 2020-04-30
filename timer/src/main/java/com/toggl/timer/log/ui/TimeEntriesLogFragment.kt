package com.toggl.timer.log.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.toggl.common.Constants.timeEntryDeletionDelayMs
import com.toggl.common.deepLinks
import com.toggl.common.performClickHapticFeedback
import com.toggl.environment.services.time.TimeService
import com.toggl.models.common.SwipeDirection
import com.toggl.timer.R
import com.toggl.timer.di.TimerComponentProvider
import com.toggl.timer.log.domain.DayHeaderViewModel
import com.toggl.timer.log.domain.FlatTimeEntryViewModel
import com.toggl.timer.log.domain.TimeEntriesLogAction
import com.toggl.timer.log.domain.TimeEntriesLogState
import com.toggl.timer.log.domain.TimeEntryGroupViewModel
import com.toggl.timer.log.domain.TimeEntryViewModel
import com.toggl.timer.log.domain.timeEntriesLogSelector
import kotlinx.android.synthetic.main.fragment_time_entries_log.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.lang.IllegalStateException
import java.util.Locale
import javax.inject.Inject

class TimeEntriesLogFragment : Fragment(R.layout.fragment_time_entries_log) {
    @Inject
    lateinit var timeService: TimeService

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val store: TimeEntriesLogStoreViewModel by viewModels { viewModelFactory }

    private var snackbar: Snackbar? = null

    private val curriedTimeEntriesSelector: suspend (TimeEntriesLogState) -> List<TimeEntryViewModel> = {
        timeEntriesLogSelector(
            it.timeEntries,
            it.projects,
            it.clients,
            timeService,
            getString(R.string.today),
            getString(R.string.yesterday),
            true,
            it.expandedGroupIds,
            it.entriesPendingDeletion
        )
    }

    override fun onAttach(context: Context) {
        (requireActivity().applicationContext as TimerComponentProvider)
            .provideTimerComponent().inject(this)
        super.onAttach(context)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        store.state
            .map(curriedTimeEntriesSelector)
            .distinctUntilChanged()
            .onEach(::updateList)
            .launchIn(lifecycleScope)

        store.state
            .map { it.editableTimeEntry != null }
            .distinctUntilChanged()
            .onEach { isEditViewExpanded ->
                if (isEditViewExpanded) {
                    this@TimeEntriesLogFragment.context?.performClickHapticFeedback()
                    findNavController().navigate(deepLinks.timeEntriesStartEditDialog)
                }
            }
            .launchIn(lifecycleScope)

        store.state
            .map { it.entriesPendingDeletion }
            .distinctUntilChanged()
            .onEach { showUndoDeletionSnackbar(it) }
            .launchIn(lifecycleScope)

        EpoxyTouchHelper.initSwiping(recycler_view)
            .leftAndRight()
            .withTargets(TimeEntryItemModel_::class.java, TimeEntryGroupModel_::class.java)
            .andCallbacks(createSwipeActionCallback(requireContext()))
    }

    private suspend fun updateList(items: List<TimeEntryViewModel>) {
        recycler_view.withModels {
            for (i in items) {
                when (i) {
                    is DayHeaderViewModel -> timeEntryHeader {
                        id(i.dayTitle)
                        text(i.dayTitle)
                        duration(i.totalDuration)
                    }
                    is FlatTimeEntryViewModel -> timeEntryItem {
                        id(i.id)
                        timeEntry(i)
                        onContinueTappedListener { store.dispatch(TimeEntriesLogAction.ContinueButtonTapped(it)) }
                        onTappedListener { store.dispatch(TimeEntriesLogAction.TimeEntryTapped(it)) }
                    }
                    is TimeEntryGroupViewModel -> timeEntryGroup {
                        id(i.groupId)
                        timeEntryGroup(i)
                        onContinueTappedListener { store.dispatch(TimeEntriesLogAction.ContinueButtonTapped(it)) }
                        onTappedListener { store.dispatch(TimeEntriesLogAction.TimeEntryGroupTapped(it)) }
                        onExpandTappedListener { store.dispatch(TimeEntriesLogAction.ToggleTimeEntryGroupTapped(it)) }
                    }
                }
            }
        }
    }

    private fun onLogItemSwiped(model: EpoxyModel<Any>, swipeDirection: SwipeDirection) {
        when (model) {
            is TimeEntryItemModel_ -> TimeEntriesLogAction.TimeEntrySwiped(model.timeEntry.id, swipeDirection)
            is TimeEntryGroupModel_ -> TimeEntriesLogAction.TimeEntryGroupSwiped(model.timeEntryGroup.timeEntryIds, swipeDirection)
            else -> throw IllegalStateException("Swiping unsupported model $model")
        }.let(store::dispatch)

        if (swipeDirection == SwipeDirection.Right) {
            // recycler_view.adapter?.notifyItemChanged(position) we need this to revert continue animation but it's it is forbidden to call this in epoxy
            recycler_view.requestModelBuild()
        }
    }

    private fun createSwipeActionCallback(context: Context): SwipeCallback {
        val leftSwipeColor = ContextCompat.getColor(context, R.color.stop_time_entry_button_background)
        val rightSwipeColor = ContextCompat.getColor(context, R.color.start_time_entry_button_background)
        val actionTextColor = ContextCompat.getColor(context, R.color.text_on_surface)
        val actionFontSize = resources.getDimension(R.dimen.swipe_action_font_size)
        val actionPadding = resources.getDimension(R.dimen.swipe_action_text_padding)

        val swipeLeftParams = SwipeActionParams(
            text = getString(R.string.delete),
            backgroundColor = leftSwipeColor,
            textColor = actionTextColor,
            fontSize = actionFontSize,
            textPadding = actionPadding
        )
        val swipeRightParams = SwipeActionParams(
            text = getString(R.string.continue_this),
            backgroundColor = rightSwipeColor,
            textColor = actionTextColor,
            fontSize = actionFontSize,
            textPadding = actionPadding
        )

        return SwipeCallback(
            swipeLeftParams = swipeLeftParams,
            swipeRightParams = swipeRightParams,
            onSwipeActionListener = ::onLogItemSwiped
        )
    }

    override fun onDestroyView() {
        snackbar = null
        super.onDestroyView()
    }

    private fun showUndoDeletionSnackbar(idsToDelete: Set<Long>) {
        snackbar?.dismiss()

        if (idsToDelete.isEmpty())
            return

        val deletionMessage = resources.getQuantityString(R.plurals.entriesDeleted, idsToDelete.size, idsToDelete.size)
        snackbar = Snackbar.make(coordinator_layout, deletionMessage, timeEntryDeletionDelayMs.toInt()).apply {
            anchorView = running_time_entry_layout
            setAction(getString(R.string.undo).toUpperCase(Locale.getDefault())) {
                store.dispatch(TimeEntriesLogAction.UndoButtonTapped)
            }
            show()
        }
    }
}
