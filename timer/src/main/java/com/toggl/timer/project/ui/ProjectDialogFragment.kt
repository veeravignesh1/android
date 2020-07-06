package com.toggl.timer.project.ui

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.toggl.architecture.extensions.select
import com.toggl.common.extensions.addInterceptingOnClickListener
import com.toggl.common.extensions.adjustForUserTheme
import com.toggl.common.extensions.performClickHapticFeedback
import com.toggl.common.extensions.requestFocus
import com.toggl.common.extensions.setOvalBackground
import com.toggl.common.extensions.setSafeText
import com.toggl.common.feature.extensions.toColor
import com.toggl.common.ui.Position
import com.toggl.common.ui.UiDrivenAutocompletePopup
import com.toggl.models.domain.Project
import com.toggl.models.domain.Workspace
import com.toggl.timer.R
import com.toggl.timer.extensions.tryHidingKeyboard
import com.toggl.timer.extensions.tryShowingKeyboardFor
import com.toggl.timer.project.domain.ColorViewModel
import com.toggl.timer.project.domain.ProjectAction
import com.toggl.timer.project.domain.ProjectColorSelector
import com.toggl.timer.project.domain.ProjectState
import com.toggl.timer.project.ui.autocomplete.WorkspaceSuggestionsAdapter
import com.toggl.timer.project.ui.autocomplete.WorkspaceViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dialog_project.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.lang.ref.WeakReference

@AndroidEntryPoint
class ProjectDialogFragment : BottomSheetDialogFragment() {

    @ExperimentalCoroutinesApi
    private val coloPickerVisibilityRequestFlow = MutableStateFlow(false)

    @ExperimentalCoroutinesApi
    private val workspacePickerRequestFlow = MutableStateFlow(false)

    @FlowPreview
    @ExperimentalCoroutinesApi
    private val adapter = ColorAdapter(::onColorTapped)
    private val store: ProjectStoreViewModel by viewModels()

    private lateinit var projectNameChangedListener: TextWatcher
    private lateinit var colorPickerAnimator: ColorPickerAnimator

    @ExperimentalCoroutinesApi
    @FlowPreview
    private lateinit var workspaceSuggestionsPopup: UiDrivenAutocompletePopup<Workspace, WorkspaceViewHolder>
    private lateinit var workspaceSuggestionsAdapter: WorkspaceSuggestionsAdapter
    private lateinit var autocompleteSuggestionsRevealAnimator: AutocompleteSuggestionsRevealAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dialog_project, container, false)

    @InternalCoroutinesApi
    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        color_palette.adapter = adapter
        color_palette.itemAnimator = null

        projectNameChangedListener = project_name_edit_text.addTextChangedListener {
            val action = ProjectAction.NameEntered(it.toString())
            store.dispatch(action)
        }

        colorPickerAnimator = ColorPickerAnimator(
            WeakReference(requireActivity().window),
            WeakReference(color_picker_container),
            WeakReference(custom_color_picker),
            resources.getDimension(R.dimen.basic_color_picker_height).toInt(),
            resources.getDimension(R.dimen.premium_color_picker_height).toInt()
        )

        autocompleteSuggestionsRevealAnimator = AutocompleteSuggestionsRevealAnimator(
            client_workspace_edit_text,
            cancel_pick,
            project_color_indicator,
            client_workspace_container,
            viewLifecycleOwner
        ) {
            project_name_edit_text?.clearFocus()
        }

        workspaceSuggestionsAdapter = WorkspaceSuggestionsAdapter {
            store.dispatch(ProjectAction.WorkspacePicked(it))
            workspacePickerRequestFlow.value = false
        }

        workspaceSuggestionsPopup = UiDrivenAutocompletePopup(
            requireContext(),
            client_workspace_edit_text,
            workspaceSuggestionsAdapter,
            viewLifecycleOwner
        ) { item, query -> item.name.contains(query) }

        project_name_edit_text.requestFocus {
            activity?.tryShowingKeyboardFor(project_name_edit_text)
        }

        color_done_button.setOnClickListener {
            coloPickerVisibilityRequestFlow.value = false
        }

        project_color_indicator.setOnClickListener {
            coloPickerVisibilityRequestFlow.value = color_picker_container.height == 0
        }

        private_chip.addInterceptingOnClickListener {
            store.dispatch(ProjectAction.PrivateProjectSwitchTapped)
        }

        workspace_chip.setOnClickListener {
            workspacePickerRequestFlow.value = true
        }

        cancel_pick.setOnClickListener {
            workspacePickerRequestFlow.value = false
        }

        create_button.setOnClickListener {
            context?.performClickHapticFeedback()
            store.dispatch(ProjectAction.DoneButtonTapped)
        }

        hue_saturation_picker.hueFlow
            .combine(hue_saturation_picker.saturationFlow) { hue, saturation ->
                ProjectAction.ColorHueSaturationChanged(hue, saturation)
            }
            .distinctUntilChanged()
            .onEach { store.dispatch(it) }
            .launchIn(lifecycleScope)

        value_picker.valueFlow
            .distinctUntilChanged()
            .map { ProjectAction.ColorValueChanged(it) }
            .onEach { store.dispatch(it) }
            .launchIn(lifecycleScope)

        store.select(ProjectColorSelector())
            .onEach { colors -> adapter.submitList(colors) }
            .launchIn(lifecycleScope)

        store.state
            .map { it.editableProject.name }
            .distinctUntilChanged()
            .onEach { project_name_edit_text.setSafeText(it) }
            .launchIn(lifecycleScope)

        store.state
            .map { it.editableProject.isPrivate }
            .distinctUntilChanged()
            .onEach { private_chip.isChecked = it }
            .launchIn(lifecycleScope)

        store.state
            .map { it.workspaces.values.toList() }
            .distinctUntilChanged()
            .onEach { workspaceSuggestionsPopup.updateAutocompleteSuggestions(it) }
            .launchIn(lifecycleScope)

        store.state
            .distinctUntilChangedBy { it.editableProject.workspaceId }
            .map { it.selectedWorkspace() }
            .onEach { workspace_chip.text = it.name }
            .launchIn(lifecycleScope)

        val hasMoreThanOneWorkspaceFlow = store.state
            .map { it.workspaces.size > 1 }
            .distinctUntilChanged()

        hasMoreThanOneWorkspaceFlow
            .onEach { hasMoreThanOneWorkspace ->
                workspace_chip.isClickable = hasMoreThanOneWorkspace
                if (!hasMoreThanOneWorkspace) {
                    workspacePickerRequestFlow.value = false
                }
            }
            .launchIn(lifecycleScope)

        store.state
            .map { it.customColor }
            .distinctUntilChanged()
            .onEach { hsv ->
                custom_project_color_indicator.setOvalBackground(hsv.toColor().adjustForUserTheme(custom_project_color_indicator.context))
                val (hue, saturation, value) = hsv
                value_picker.hue = hue
                value_picker.saturation = saturation
                value_picker.value = value
                hue_saturation_picker.hue = hue
                hue_saturation_picker.saturation = saturation
                hue_saturation_picker.value = value
            }.launchIn(lifecycleScope)

        val colorFlow = store.state.map { it.editableProject.color }.distinctUntilChanged()

        colorFlow
            .onEach {
                project_color_indicator.setOvalBackground(it.adjustForUserTheme(custom_project_color_indicator.context))
            }
            .launchIn(lifecycleScope)

        val colorIsPremiumFlow = colorFlow.map {
            !Project.defaultColors.contains(it)
        }.distinctUntilChanged()

        coloPickerVisibilityRequestFlow
            .combine(colorIsPremiumFlow) { shouldShow, colorIsPremium -> shouldShow to colorIsPremium }
            .onEach { (shouldShow, colorIsPremium) -> toggleColorPicker(shouldShow, colorIsPremium) }
            .launchIn(lifecycleScope)

        workspacePickerRequestFlow
            .combine(hasMoreThanOneWorkspaceFlow) { wasRequested, canShow -> wasRequested && canShow }
            .onEach { shouldShow -> toggleWorkspaceAutocomplete(shouldShow) }
            .launchIn(lifecycleScope)

        val bottomSheetBehavior = (dialog as BottomSheetDialog).behavior
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun toggleWorkspaceAutocomplete(shouldShow: Boolean) {
        if (shouldShow) {
            workspaceSuggestionsPopup.isShowing = true
            client_workspace_edit_text.hint = getString(R.string.search_for_workspaces)
            autocompleteSuggestionsRevealAnimator.revealEditText()
            workspaceSuggestionsPopup.show(Position.Above)
        } else {
            workspaceSuggestionsPopup.isShowing = false
            workspaceSuggestionsPopup.dismiss()
            autocompleteSuggestionsRevealAnimator.hideEditText()
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onDestroyView() {
        super.onDestroyView()

        colorPickerAnimator.finish()
        project_name_edit_text.removeTextChangedListener(projectNameChangedListener)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun toggleColorPicker(shouldShowPicker: Boolean, currentColorIsPremium: Boolean) {
        if (shouldShowPicker) {
            colorPickerAnimator.showPicker(currentColorIsPremium) {
                project_name_edit_text.clearFocus()
                activity?.tryHidingKeyboard(project_name_edit_text)
            }
        } else {
            project_name_edit_text.requestFocus()
            activity?.tryShowingKeyboardFor(project_name_edit_text)
            colorPickerAnimator.hidePicker()
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun onColorTapped(colorViewModel: ColorViewModel) {
        when (colorViewModel) {
            is ColorViewModel.DefaultColor -> {
                store.dispatch(ProjectAction.ColorPicked(colorViewModel.color))
                coloPickerVisibilityRequestFlow.value = false
            }
            is ColorViewModel.CustomColor -> {
                store.dispatch(listOf(
                    ProjectAction.ColorValueChanged(value_picker.value),
                    ProjectAction.ColorHueSaturationChanged(hue_saturation_picker.hue, hue_saturation_picker.saturation)
                ))
            }
            ColorViewModel.PremiumLocked -> {
                // TODO Show the awareness popup
            }
        }
    }

    private fun ProjectState.selectedWorkspace(): Workspace {
        return workspaces[editableProject.workspaceId]
            ?: throw IllegalStateException("Editable time entry's workspace Id doesn't exist")
    }
}
