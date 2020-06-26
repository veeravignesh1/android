package com.toggl.common.feature.navigation

import android.os.Bundle
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Navigator.Name("bottom_sheet")
class BottomSheetNavigator @Inject constructor() : Navigator<BottomSheetNavigator.Destination>() {
    var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ): NavDestination? {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        return destination
    }

    override fun popBackStack(): Boolean {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        return true
    }

    override fun createDestination(): Destination =
        Destination()

    class Destination : NavDestination("bottom_sheet")
}