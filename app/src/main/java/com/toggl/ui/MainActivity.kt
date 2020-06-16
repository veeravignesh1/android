package com.toggl.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.toggl.R
import com.toggl.TogglApplication
import com.toggl.architecture.core.Store
import com.toggl.common.BottomNavigationProvider
import com.toggl.common.deepLinks
import com.toggl.domain.AppAction
import com.toggl.domain.AppState
import com.toggl.domain.loading.LoadingAction
import com.toggl.timer.suggestions.domain.SuggestionsAction
import com.toggl.timer.suggestions.ui.SuggestionsStoreViewModel
import javax.inject.Inject
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : AppCompatActivity(R.layout.main_activity), BottomNavigationProvider {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var store: Store<AppState, AppAction>

    private val suggestionsStore: SuggestionsStoreViewModel by viewModels { viewModelFactory }

    override var isBottomNavigationVisible: Boolean
        get() = bottom_navigation.isVisible
        set(value) { bottom_navigation.isVisible = value }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as TogglApplication).appComponent.inject(this)

        store.dispatch(AppAction.Loading(LoadingAction.StartLoading))

        bottom_navigation.setOnNavigationItemSelectedListener(::changeTab)
        bottom_navigation.setOnNavigationItemReselectedListener(::scrollUpOnTab)
    }

    private fun changeTab(menuItem: MenuItem): Boolean {
        nav_host_fragment.findNavController().navigate(
            when (menuItem.itemId) {
                R.id.timer_tab -> deepLinks.timeEntriesLog
                R.id.reports_tab -> deepLinks.reports
                R.id.calendar_tab -> deepLinks.calendar
                else -> throw NotImplementedError()
            }
        )
        return true
    }

    override fun onResume() {
        super.onResume()
        suggestionsStore.dispatch(SuggestionsAction.LoadSuggestions)
    }

    private fun scrollUpOnTab(menuItem: MenuItem) {
        Log.i("MainActivity", menuItem.title.toString())
    }
}
