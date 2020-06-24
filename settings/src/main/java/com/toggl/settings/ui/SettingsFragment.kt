package com.toggl.settings.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.toggl.settings.R
import com.toggl.settings.di.SettingsComponentProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val store: SettingsStoreViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        (requireActivity().applicationContext as SettingsComponentProvider)
                .provideSettingsComponent().inject(this)
        super.onAttach(context)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
