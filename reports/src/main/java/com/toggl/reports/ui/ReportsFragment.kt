package com.toggl.reports.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.toggl.architecture.extensions.select
import com.toggl.common.feature.compose.extensions.createComposeView
import com.toggl.reports.domain.ReportsSelector
import com.toggl.reports.ui.composables.ReportsPage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReportsFragment : Fragment() {

    @Inject @JvmField var selector: ReportsSelector? = null

    private val store: ReportsStoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createComposeView { statusBarHeight, _ ->

        val viewModels = store.state.select(selector!!)

        ReportsPage(viewModels, statusBarHeight, store::dispatch)
    }
}
