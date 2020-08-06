package com.toggl.reports.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.graphics.Color
import androidx.ui.layout.padding
import com.toggl.common.feature.compose.extensions.createComposeView
import com.toggl.reports.domain.ReportsSelector
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReportsFragment : Fragment() {

    @Inject lateinit var selector: ReportsSelector

    private val store: ReportsStoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createComposeView { statusBarHeight, _ ->
        Box(
            modifier = Modifier.padding(top = statusBarHeight),
            backgroundColor = Color.Magenta
        )
    }
}
