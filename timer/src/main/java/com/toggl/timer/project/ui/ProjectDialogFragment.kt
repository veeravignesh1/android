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
import com.toggl.common.extensions.addInterceptingOnClickListener
import com.toggl.common.extensions.performClickHapticFeedback
import com.toggl.timer.R
import com.toggl.timer.di.TimerComponentProvider
import com.toggl.timer.project.domain.ProjectAction
import kotlinx.android.synthetic.main.fragment_dialog_project.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ProjectDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val store: ProjectStoreViewModel by viewModels { viewModelFactory }

    private lateinit var projectNameChangedListener: TextWatcher

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

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectNameChangedListener = project_name_edit_text.addTextChangedListener {
            val action = ProjectAction.NameEntered(it.toString())
            store.dispatch(action)
        }

        private_chip.addInterceptingOnClickListener {
            store.dispatch(ProjectAction.PrivateProjectSwitchTapped)
        }

        create_button.setOnClickListener {
            context?.performClickHapticFeedback()
            store.dispatch(ProjectAction.DoneButtonTapped)
        }

        store.state
            .mapNotNull { it.editableProject.isPrivate }
            .distinctUntilChanged()
            .onEach { private_chip.isChecked = it }
            .launchIn(lifecycleScope)

        val bottomSheetBehavior = (dialog as BottomSheetDialog).behavior
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroyView() {
        super.onDestroyView()

        project_name_edit_text.removeTextChangedListener(projectNameChangedListener)
    }
}
