package com.toggl.timer.startedit.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupWindow.INPUT_METHOD_NEEDED
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.toggl.architecture.extensions.select
import com.toggl.common.Constants.elapsedTimeIndicatorUpdateDelayMs
import com.toggl.common.RecyclerViewPopup
import com.toggl.common.above
import com.toggl.common.deepLinks
import com.toggl.common.extensions.addInterceptingOnClickListener
import com.toggl.common.extensions.performClickHapticFeedback
import com.toggl.common.extensions.requestFocus
import com.toggl.common.extensions.setSafeText
import com.toggl.common.sheet.AlphaSlideAction
import com.toggl.common.sheet.BottomSheetCallback
import com.toggl.common.sheet.OnStateChangedAction
import com.toggl.common.feature.timeentry.extensions.isRepresentingGroup
import com.toggl.common.feature.timeentry.extensions.wasNotYetPersisted
import com.toggl.common.showTooltip
import com.toggl.environment.services.time.TimeService
import com.toggl.models.domain.EditableTimeEntry
import com.toggl.models.domain.Workspace
import com.toggl.models.domain.WorkspaceFeature
import com.toggl.timer.R
import com.toggl.timer.di.TimerComponentProvider
import com.toggl.timer.extensions.formatForDisplaying
import com.toggl.common.extensions.formatForDisplayingDate
import com.toggl.common.extensions.formatForDisplayingTime
import com.toggl.timer.extensions.tryHidingKeyboard
import com.toggl.timer.extensions.tryShowingKeyboardFor
import com.toggl.timer.startedit.domain.DateTimePickMode
import com.toggl.timer.startedit.domain.ProjectTagChipSelector
import com.toggl.timer.startedit.domain.StartEditAction
import com.toggl.timer.startedit.domain.StartEditState
import com.toggl.timer.startedit.domain.SuggestionsSelector
import com.toggl.timer.startedit.domain.TemporalInconsistency
import com.toggl.timer.startedit.ui.autocomplete.AutocompleteSuggestionViewHolder
import com.toggl.timer.startedit.ui.autocomplete.SuggestionsAdapter
import com.toggl.timer.startedit.ui.chips.ChipAdapter
import com.toggl.timer.startedit.ui.chips.ChipViewModel
import kotlinx.android.synthetic.main.bottom_control_panel_layout.*
import kotlinx.android.synthetic.main.fragment_dialog_start_edit.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import java.time.Duration
import java.time.OffsetDateTime
import javax.inject.Inject
import kotlin.contracts.ExperimentalContracts
import kotlin.math.min
import com.toggl.common.android.R as CommonR

@ExperimentalContracts
class StartEditDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var projectTagChipSelector: ProjectTagChipSelector

    @Inject
    lateinit var suggestionsSelector: SuggestionsSelector

    @Inject
    lateinit var timeService: TimeService
    private var timeIndicatorScheduledUpdate: Job? = null

    private val store: StartEditStoreViewModel by viewModels { viewModelFactory }

    private var editDialog: Dialog? = null

    private val dispatchingCancelListener: DialogInterface.OnCancelListener = DialogInterface.OnCancelListener {
        store.dispatch(StartEditAction.DateTimePickingCancelled)
    }

    private val adapter = ChipAdapter(::onChipTapped)
    private val suggestionsAdapter = SuggestionsAdapter { store.dispatch(StartEditAction.AutocompleteSuggestionTapped(it)) }

    private var suggestionHeight: Int = 0
    private var suggestionsPopupMaxHeight: Int = 0
    private lateinit var suggestionsPopup: RecyclerViewPopup<AutocompleteSuggestionViewHolder>
    private val bottomSheetCallback = BottomSheetCallback()

    private lateinit var bottomControlPanelAnimator: BottomControlPanelAnimator
    private lateinit var hideableStopViews: List<View>
    private lateinit var extentedTimeOptions: List<View>

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onAttach(context: Context) {
        (requireActivity().applicationContext as TimerComponentProvider)
            .provideTimerComponent().inject(this)
        super.onAttach(context)

        val activeButtonColor = ContextCompat.getColor(context, CommonR.color.button_active)
        val inactiveButtonColor = ContextCompat.getColor(context, CommonR.color.button_inactive)
        bottomControlPanelAnimator = BottomControlPanelAnimator(activeButtonColor, inactiveButtonColor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dialog_start_edit, container, false)

    @ExperimentalCoroutinesApi
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).also { bottomSheetDialog: BottomSheetDialog ->
            store.state
                .map { BottomControlPanelParams(it.editableTimeEntry, it.isEditableInProWorkspace()) }
                .take(1)
                .onEach {
                    bottomSheetDialog.setOnShowListener { dialogInterface ->
                        dialogInterface.attachBottomView(bottomSheetDialog, R.layout.bottom_control_panel_layout, it)
                    }
                }
                .launchIn(lifecycleScope)
        }
    }

    @kotlinx.coroutines.FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        suggestionsPopup = RecyclerViewPopup(
            requireContext(),
            time_entry_description,
            R.layout.suggestions_popup,
            R.id.suggestions_recycler_view,
            suggestionsAdapter
        )

        suggestionHeight = resources.getDimensionPixelSize(R.dimen.suggestions_popup_item_height)
        suggestionsPopupMaxHeight = resources.getDimensionPixelSize(R.dimen.suggestions_popup_max_height)
        suggestionsPopup.inputMethodMode = INPUT_METHOD_NEEDED
        suggestionsPopup.isOutsideTouchable = true

        chip_recycler_view.adapter = adapter
        hideableStopViews = listOf(stop_divider, stop_date_label)
        extentedTimeOptions = listOf(
            start_header,
            start_time_label,
            start_divider,
            start_date_label,
            stop_header,
            stop_time_label,
            stop_divider,
            stop_date_label,
            wheel_background,
            wheel_foreground,
            wheel_duration_input
        )

        extentedTimeOptions
            .forEach { bottomSheetCallback.addOnSlideAction(AlphaSlideAction(it, false)) }

        bottomSheetCallback.addOnSlideAction(AlphaSlideAction(billable_chip, false))

        bottomSheetCallback.addOnStateChangedAction(object : OnStateChangedAction {
            override fun onStateChanged(sheet: View, newState: Int) {
                updateSuggestionPopupVisibility(newState)
            }
        })

        store.state
            .mapNotNull { it.editableTimeEntry.description }
            .onEach { time_entry_description.setSafeText(it) }
            .launchIn(lifecycleScope)

        store.state
            .map { it.editableTimeEntry.editableProject != null }
            .distinctUntilChanged()
            .drop(1)
            .onEach { projectDialogIsVisible ->
                if (projectDialogIsVisible) {
                    findNavController().navigate(deepLinks.timeEntriesProjectDialog)
                } else {
                    findNavController().popBackStack()
                }
            }
            .launchIn(lifecycleScope)

        store.state
            .mapNotNull { it.editableTimeEntry }
            .distinctUntilChanged { old, new -> old.ids == new.ids && old.startTime == new.startTime }
            .onEach {
                scheduleTimeEntryStartTimeAndDurationIndicators(it)
                handleStartStopElementsState(it)
            }
            .launchIn(lifecycleScope)

        store.state
            .distinctUntilChanged(::projectsOrTagsChanged)
            .select(projectTagChipSelector)
            .onEach { adapter.submitList(it) }
            .launchIn(lifecycleScope)

        store.state
            .select(suggestionsSelector)
            .onEach { suggestionsAdapter.submitList(it) }
            .launchIn(lifecycleScope)

        store.state
            .map { it.autocompleteSuggestions.size }
            .onEach { showSuggestionsPopup(it) }
            .launchIn(lifecycleScope)

        store.state
            .distinctUntilChanged { old, new -> old.dateTimePickMode == new.dateTimePickMode }
            .onEach { startEditingTimeDate(it.dateTimePickMode, it.editableTimeEntry) }
            .launchIn(lifecycleScope)

        store.state
            .map { it.isEditableInProWorkspace() }
            .distinctUntilChanged()
            .onEach { shouldBillableOptionsShow -> billable_chip.isVisible = shouldBillableOptionsShow }
            .launchIn(lifecycleScope)

        store.state
            .map { it.editableTimeEntry.billable }
            .distinctUntilChanged()
            .onEach { billable_chip.isChecked = it }
            .launchIn(lifecycleScope)

        store.state
            .map { it.temporalInconsistency }
            .distinctUntilChanged()
            .onEach {
                val textRes = when (it) {
                    TemporalInconsistency.StartTimeAfterCurrentTime -> R.string.start_time_after_current_time_warning
                    TemporalInconsistency.StartTimeAfterStopTime -> R.string.start_time_after_stop_time_warning
                    TemporalInconsistency.StopTimeBeforeStartTime -> R.string.stop_time_before_start_time_warning
                    TemporalInconsistency.DurationTooLong -> R.string.duration_too_long_warning
                    TemporalInconsistency.None -> null
                }
                textRes?.let { tr ->
                    Toast.makeText(context, tr, Toast.LENGTH_SHORT).show()
                }
            }
            .launchIn(lifecycleScope)

        time_entry_description
            .onDescriptionChanged
            .distinctUntilChanged()
            .onEach { store.dispatch(it) }
            .launchIn(lifecycleScope)

        with(wheel_foreground) {
            isEditingFlow
                .distinctUntilChanged()
                .onEach { nested_scrollview.requestDisallowInterceptTouchEvent(it) }
                .launchIn(lifecycleScope)

            startTimeFlow
                .distinctUntilChanged()
                .onEach { store.dispatch(StartEditAction.WheelChangedStartTime(it)) }
                .launchIn(lifecycleScope)

            endTimeFlow
                .distinctUntilChanged()
                .map { Duration.between(wheel_foreground.startTime, it) }
                .onEach { store.dispatch(StartEditAction.WheelChangedEndTime(it)) }
                .launchIn(lifecycleScope)
        }

        billable_chip.addInterceptingOnClickListener {
            store.dispatch(StartEditAction.BillableTapped)
        }

        val bottomSheetBehavior = (dialog as BottomSheetDialog).behavior
        with(bottomSheetBehavior) {
            addBottomSheetCallback(bottomSheetCallback)
            skipCollapsed = false
            peekHeight = resources.getDimension(R.dimen.time_entry_edit_half_expanded_height).toInt()
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        with(time_entry_description) {
            requestFocus {
                activity?.tryShowingKeyboardFor(time_entry_description)
            }
        }

        close_action.setOnClickListener {
            store.dispatch(StartEditAction.CloseButtonTapped)
        }

        val clearNumericInputFocusBottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    wheel_duration_input.clearFocus()
                }
            }
        }

        with(wheel_duration_input) {
            durationFlow
                .distinctUntilChanged()
                .onEach { store.dispatch(StartEditAction.DurationInputted(it)) }
                .launchIn(lifecycleScope)

            setOnFocusChangeListener { numericInput, hasFocus ->
                if (hasFocus) {
                    dialog?.window?.setSoftInputMode(SOFT_INPUT_ADJUST_PAN)
                    bottomSheetBehavior.addBottomSheetCallback(clearNumericInputFocusBottomSheetCallback)
                    activity?.tryShowingKeyboardFor(numericInput, InputMethodManager.SHOW_FORCED)
                } else {
                    dialog?.window?.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
                    bottomSheetBehavior.removeBottomSheetCallback(clearNumericInputFocusBottomSheetCallback)
                    activity?.tryHidingKeyboard(numericInput)
                }
                this@StartEditDialogFragment.isCancelable = !hasFocus
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun updateSuggestionPopupVisibility(@Suppress("UNUSED_PARAMETER") bottomSheetState: Int) {
        val popupCanBeVisible = bottomSheetState == BottomSheetBehavior.STATE_COLLAPSED ||
            bottomSheetState == BottomSheetBehavior.STATE_EXPANDED
        if (popupCanBeVisible && suggestionsAdapter.itemCount > 0) {
            calculateAndShowPopup(suggestionsAdapter.itemCount)
        } else {
            suggestionsPopup.dismiss()
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun showSuggestionsPopup(numberOfSuggestions: Int) {
        val popupIsVisible = numberOfSuggestions > 0
        if (popupIsVisible) {
            calculateAndShowPopup(numberOfSuggestions)
        } else {
            suggestionsPopup.dismiss()
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun calculateAndShowPopup(numberOfSuggestions: Int) {
        with(time_entry_description) {

            val popupXPosition = layout?.let {
                val currentCursorOffset = time_entry_description.selectionStart
                it.getPrimaryHorizontal(currentCursorOffset).toInt()
            } ?: paddingStart

            val locationOnScreen = intArrayOf(0, 0)
            getLocationOnScreen(locationOnScreen)
            val yPositionOfTextView = locationOnScreen[1]
            val popupHeight = min(numberOfSuggestions * suggestionHeight, suggestionsPopupMaxHeight)

            val positionAboveTextView = yPositionOfTextView - popupHeight
            val positionBelowTextView = yPositionOfTextView + height
            val shouldShowBelowTextView = positionAboveTextView < 0
            val popupYPosition = if (shouldShowBelowTextView) positionBelowTextView else positionAboveTextView

            suggestionsPopup.show(
                x = popupXPosition,
                y = popupYPosition,
                width = ViewGroup.LayoutParams.WRAP_CONTENT,
                height = popupHeight
            )
        }
    }

    private fun projectsOrTagsChanged(old: StartEditState, new: StartEditState): Boolean {
        val oldEditableTimeEntry = old.editableTimeEntry
        val newEditableTimeEntry = new.editableTimeEntry

        return oldEditableTimeEntry.projectId == newEditableTimeEntry.projectId &&
            oldEditableTimeEntry.tagIds == newEditableTimeEntry.tagIds
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onDestroyView() {
        bottomSheetCallback.clear()
        store.dispatch(StartEditAction.DialogDismissed)
        time_entry_description.clearDescriptionChangedListeners()
        dismissEditDialog()
        super.onDestroyView()
    }

    override fun onCancel(dialog: DialogInterface) {
        (dialog as BottomSheetDialog).setOnShowListener(null)
        super.onCancel(dialog)
    }

    @ExperimentalCoroutinesApi
    private fun DialogInterface.attachBottomView(
        bottomSheetDialog: BottomSheetDialog,
        @LayoutRes layoutToAttach: Int,
        bottomControlPanelParams: BottomControlPanelParams
    ) {
        val coordinator =
            (this as BottomSheetDialog).findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)
        val containerLayout =
            this.findViewById<FrameLayout>(com.google.android.material.R.id.container)
        val bottomControlPanel = bottomSheetDialog.layoutInflater.inflate(layoutToAttach, null)

        val billableButton = bottomControlPanel.findViewById<ImageView>(R.id.billable_action)
        billableButton.isVisible = bottomControlPanelParams.isProWorkspace

        store.state
            .mapNotNull { it.editableTimeEntry.billable }
            .distinctUntilChanged()
            .onEach { setBillableButtonColor(billableButton, it) }
            .map { if (it) R.string.non_billable else R.string.billable }
            .onEach { tooltipText ->
                billableButton.setOnClickListener {
                    store.dispatch(StartEditAction.BillableTapped)
                    showTooltip(tooltipText).above(billableButton)
                }
            }
            .launchIn(lifecycleScope)

        bottomControlPanel.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply { gravity = Gravity.BOTTOM }
        containerLayout?.addView(bottomControlPanel)

        bottomControlPanel.post {
            (coordinator?.layoutParams as ViewGroup.MarginLayoutParams).apply {
                bottomControlPanel.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                this.bottomMargin = bottomControlPanel.measuredHeight
                containerLayout?.requestLayout()
            }

            done_action.setOnClickListener {
                context.performClickHapticFeedback()
                store.dispatch(StartEditAction.DoneButtonTapped)
            }

            project_action.setOnClickListener {
                store.dispatch(StartEditAction.ProjectButtonTapped)
            }

            tag_action.setOnClickListener {
                store.dispatch(StartEditAction.TagButtonTapped)
            }
        }
    }

    private fun onChipTapped(chip: ChipViewModel) {
        store.dispatch(
            when (chip) {
                is ChipViewModel.AddTag -> StartEditAction.AddTagChipTapped
                is ChipViewModel.AddProject -> StartEditAction.AddProjectChipTapped
                is ChipViewModel.Tag -> TODO()
                is ChipViewModel.Project -> TODO()
            }
        )
    }

    private fun startEditingTimeDate(dateTimePickMode: DateTimePickMode, editableTimeEntry: EditableTimeEntry) {
        when (dateTimePickMode) {
            DateTimePickMode.None -> dismissEditDialog()
            DateTimePickMode.StartTime -> startEditingTime(editableTimeEntry.startTimeOrNow())
            DateTimePickMode.StartDate -> startEditingDate(
                editableTimeEntry.startTimeOrNow(),
                maxTime = editableTimeEntry.endTimeOrNow()
            )
            DateTimePickMode.EndTime -> startEditingTime(editableTimeEntry.endTimeOrNow()!!)
            DateTimePickMode.EndDate -> startEditingDate(
                editableTimeEntry.endTimeOrNow()!!,
                minTime = editableTimeEntry.startTime!!
            )
        }
    }

    private fun dismissEditDialog() {
        editDialog?.dismiss()
        editDialog = null
    }

    private fun startEditingTime(initialTime: OffsetDateTime) {
        val onTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val changedTime = initialTime
                .withHour(hourOfDay)
                .withMinute(minute)
            store.dispatch(StartEditAction.DateTimePicked(changedTime))
        }
        TimePickerDialog(
            requireContext(),
            onTimeSetListener,
            initialTime.hour,
            initialTime.minute,
            true
        ).run {
            setOnCancelListener(dispatchingCancelListener)
            show()
            editDialog = this
        }
    }

    private fun startEditingDate(
        initialTime: OffsetDateTime,
        minTime: OffsetDateTime? = null,
        maxTime: OffsetDateTime? = null
    ) {
        val onDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val changedTime = initialTime
                .withYear(year)
                .withMonth(month + 1)
                .withDayOfMonth(dayOfMonth)
            store.dispatch(StartEditAction.DateTimePicked(changedTime))
        }
        DatePickerDialog(
            requireContext(),
            onDateSetListener,
            initialTime.year,
            initialTime.monthValue - 1,
            initialTime.dayOfMonth
        ).run {
            maxTime?.let { datePicker.maxDate = maxTime.toEpochMillisecond() }
            minTime?.let { datePicker.minDate = minTime.toEpochMillisecond() }

            setOnCancelListener(dispatchingCancelListener)
            show()
            editDialog = this
        }
    }

    private fun setBillableButtonColor(billableButton: ImageView, isBillable: Boolean) {
        bottomControlPanelAnimator.animateBackground(billableButton.background, isBillable)
        bottomControlPanelAnimator.animateColorFilter(billableButton, isBillable)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun scheduleTimeEntryStartTimeAndDurationIndicators(editableTimeEntry: EditableTimeEntry) {
        timeIndicatorScheduledUpdate?.cancel()
        timeIndicatorScheduledUpdate = lifecycleScope.launchWhenCreated {
            while (true) {
                val durationForDisplaying = editableTimeEntry.getDurationForDisplaying()
                time_indicator.setTextIfDifferent(durationForDisplaying.formatForDisplaying())

                if (!editableTimeEntry.isRepresentingGroup() && editableTimeEntry.startTime == null) {
                    setTextOnStartTimeLabels(timeService.now())
                }

                if (!wheel_foreground.isEditing()) {
                    val startTime = editableTimeEntry.startTime ?: timeService.now()
                    val endTime = startTime + durationForDisplaying
                    wheel_foreground.startTime = editableTimeEntry.startTime ?: timeService.now()
                    wheel_foreground.endTime = endTime
                    wheel_foreground.isRunning = editableTimeEntry.duration == null
                }

                if (!wheel_duration_input.hasFocus()) {
                    wheel_duration_input.setDuration(durationForDisplaying)
                }

                delay(elapsedTimeIndicatorUpdateDelayMs)
            }
        }
    }

    private fun setTextOnTimeDateLabels(timeLabel: TextView, dateLabel: TextView, time: OffsetDateTime) {
        timeLabel.setTextIfDifferent(time.formatForDisplayingTime())
        dateLabel.setTextIfDifferent(time.formatForDisplayingDate())
    }

    private fun setTextOnStartTimeLabels(startTime: OffsetDateTime?) =
        setTextOnTimeDateLabels(start_time_label, start_date_label, startTime ?: timeService.now())

    private fun handleStartStopElementsState(editableTimeEntry: EditableTimeEntry) {
        with(editableTimeEntry) {

            if (isRepresentingGroup()) {
                extentedTimeOptions.forEach { it.isVisible = false }
                return
            }

            mapOf(
                start_time_label to DateTimePickMode.StartTime,
                stop_time_label to DateTimePickMode.EndTime,
                start_date_label to DateTimePickMode.StartDate,
                stop_date_label to DateTimePickMode.EndDate
            ).onEach { it.setPickerTappedActionOnLabel() }

            setTextOnStartTimeLabels(startTime)

            hideableStopViews.forEach { it.isVisible = duration != null }
            when (duration) {
                null -> {
                    stop_time_label.text =
                        if (wasNotYetPersisted()) getString(R.string.set_stop_time) else getString(R.string.stop)

                    stop_time_label.setOnClickListener {
                        store.dispatch(StartEditAction.StopButtonTapped)
                    }
                }
                else -> {
                    val endTime = startTime!!.plus(duration)
                    setTextOnTimeDateLabels(stop_time_label, stop_date_label, endTime)
                }
            }
        }
    }

    private fun Workspace.isPro() = this.features.indexOf(WorkspaceFeature.Pro) != -1
    private fun StartEditState.isEditableInProWorkspace() = this.editableTimeEntry.workspaceId.run {
        this@isEditableInProWorkspace.workspaces[this]?.isPro()
    } ?: false

    private fun EditableTimeEntry.isRunning() = this.ids.size == 1 && this.startTime != null && this.duration == null
    private fun EditableTimeEntry.isStopped() = this.startTime != null && this.duration != null
    private fun EditableTimeEntry.getDurationForDisplaying() = when {
        this.duration != null -> this.duration!!
        this.wasNotYetPersisted() && this.startTime == null -> Duration.ZERO
        this.startTime != null -> Duration.between(this.startTime, timeService.now())
        else -> throw IllegalStateException("Editable time entry must either have a duration, a start time or not be started yet (have no ids)")
    }

    private fun EditableTimeEntry.startTimeOrNow(): OffsetDateTime = this.startTime ?: timeService.now()
    private fun EditableTimeEntry.endTimeOrNow(): OffsetDateTime? = when {
        this.isStopped() -> this.startTime!!.plus(this.duration)
        this.isRunning() || this.wasNotYetPersisted() -> timeService.now()
        else -> null
    }

    private fun TextView.setTextIfDifferent(newText: String) {
        if (this.text != newText) {
            this.text = newText
        }
    }

    private fun Map.Entry<TextView, DateTimePickMode>.setPickerTappedActionOnLabel() {
        val (label, action) = this
        label.setOnClickListener {
            store.dispatch(StartEditAction.PickerTapped(action))
        }
    }

    private fun OffsetDateTime.toEpochMillisecond() = this.toEpochSecond() * 1000

    private data class BottomControlPanelParams(val editableTimeEntry: EditableTimeEntry, val isProWorkspace: Boolean)
}
