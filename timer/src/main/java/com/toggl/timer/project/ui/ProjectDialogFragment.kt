package com.toggl.timer.project.ui

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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
import com.toggl.models.domain.Project
import com.toggl.timer.R
import com.toggl.timer.di.TimerComponentProvider
import com.toggl.timer.extensions.tryHidingKeyboard
import com.toggl.timer.extensions.tryShowingKeyboardFor
import com.toggl.timer.project.domain.ColorViewModel
import com.toggl.timer.project.domain.ProjectAction
import com.toggl.timer.project.domain.ProjectColorSelector
import kotlinx.android.synthetic.main.fragment_dialog_project.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.lang.ref.WeakReference
import javax.inject.Inject

class ProjectDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @ExperimentalCoroutinesApi
    private val coloPickerVisibilityRequestFlow = MutableStateFlow(false)

    @FlowPreview
    @ExperimentalCoroutinesApi
    private val adapter = ColorAdapter(::onColorTapped)
    private val store: ProjectStoreViewModel by viewModels { viewModelFactory }

    private lateinit var projectNameChangedListener: TextWatcher
    private lateinit var colorPickerAnimator: ColorPickerAnimator

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

        val bottomSheetBehavior = (dialog as BottomSheetDialog).behavior
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
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
}
