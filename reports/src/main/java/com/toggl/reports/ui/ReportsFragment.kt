package com.toggl.reports.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.toggl.reports.domain.ReportsAction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportsFragment : Fragment() {
    private val store: ReportsStoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = View(context).apply { background = ColorDrawable(Color.parseColor("#FF00FF")) }

    override fun onResume() {
        super.onResume()
        store.dispatch(ReportsAction.ViewAppeared)
    }
}
