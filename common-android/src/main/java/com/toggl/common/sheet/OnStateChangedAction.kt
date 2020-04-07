package com.toggl.common.sheet

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

interface OnStateChangedAction {
    fun onStateChanged(sheet: View, @BottomSheetBehavior.State newState: Int)
}