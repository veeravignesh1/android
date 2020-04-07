package com.toggl.timer.start.ui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.getSystemService
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.toggl.common.sheet.AlphaSlideAction
import com.toggl.common.sheet.BottomSheetCallback
import com.toggl.common.sheet.OnStateChangedAction
import com.toggl.timer.R
import com.toggl.timer.common.domain.EditableTimeEntry
import com.toggl.timer.di.TimerComponentProvider
import com.toggl.timer.start.domain.StartTimeEntryAction
import kotlinx.android.synthetic.main.bottom_control_panel_layout.*
import kotlinx.android.synthetic.main.fragment_dialog_start_time_entry.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class StartTimeEntryDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val store: StartTimeEntryStoreViewModel by viewModels { viewModelFactory }

    private var descriptionChangeListener: TextWatcher? = null

    private val bottomSheetCallback = BottomSheetCallback()

    override fun onAttach(context: Context) {
        (requireActivity().applicationContext as TimerComponentProvider)
            .provideTimerComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dialog_start_time_entry, container, false)

    @ExperimentalCoroutinesApi
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).also { bottomSheetDialog: BottomSheetDialog ->
            store.state
                .mapNotNull { it.editableTimeEntry }
                .take(1)
                .onEach { editableTimeEntry ->
                    bottomSheetDialog.setOnShowListener { dialogInterface ->
                        dialogInterface.attachBottomView(bottomSheetDialog, R.layout.bottom_control_panel_layout, editableTimeEntry)
                    }
                }
                .launchIn(lifecycleScope)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetCallback.addOnSlideAction(AlphaSlideAction(extended_options, false))
        bottomSheetCallback.addOnStateChangedAction(object : OnStateChangedAction {
            override fun onStateChanged(sheet: View, newState: Int) {
                extended_options.isInvisible = newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN
            }
        })

        val bottomSheetBehavior = (dialog as BottomSheetDialog).behavior
        with(bottomSheetBehavior) {
            addBottomSheetCallback(bottomSheetCallback)
            skipCollapsed = false
            peekHeight = resources.getDimension(R.dimen.time_entry_edit_half_expanded_height).toInt()
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        with(time_entry_description) {
            setOnFocusChangeListener { _, _ ->
                post {
                    activity?.getSystemService<InputMethodManager>()
                        ?.showSoftInput(time_entry_description, InputMethodManager.SHOW_IMPLICIT)
                }
            }
            requestFocus()
            descriptionChangeListener = time_entry_description.addTextChangedListener {
                val action = StartTimeEntryAction.DescriptionEntered(text.toString())
                store.dispatch(action)
            }
        }

        close_action.setOnClickListener {
            store.dispatch(StartTimeEntryAction.CloseButtonTapped)
        }

        store.state
            .filterNot { it.editableTimeEntry?.ids.isNullOrEmpty() }
            .map { it.editableTimeEntry?.description }
            .filter { !it.isNullOrEmpty() }
            .take(1)
            .onEach { time_entry_description.setText(it) }
            .launchIn(lifecycleScope)

        store.state
            .filter { it.editableTimeEntry == null }
            .distinctUntilChanged()
            .onEach {
                if (isVisible) {
                    findNavController().popBackStack()
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        bottomSheetCallback.clear()
        store.dispatch(StartTimeEntryAction.DialogDismissed)
        time_entry_description.removeTextChangedListener(descriptionChangeListener)
        super.onDestroyView()
    }

    override fun onCancel(dialog: DialogInterface) {
        (dialog as BottomSheetDialog).setOnShowListener(null)
        super.onCancel(dialog)
    }

    private fun DialogInterface.attachBottomView(
        bottomSheetDialog: BottomSheetDialog,
        @LayoutRes layoutToAttach: Int,
        editableTimeEntry: EditableTimeEntry
    ) {
        val coordinator =
            (this as BottomSheetDialog).findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)
        val containerLayout =
            this.findViewById<FrameLayout>(com.google.android.material.R.id.container)
        val bottomControlPanel = bottomSheetDialog.layoutInflater.inflate(layoutToAttach, null)

        bottomControlPanel.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM
        }
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

            val isNewTimeEntry = editableTimeEntry.ids.isNullOrEmpty()
            done_action.isVisible = isNewTimeEntry
            done_action.setOnClickListener {
                store.dispatch(StartTimeEntryAction.DoneButtonTapped)
            }
        }
    }
}