package com.toggl.common.sheet

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetCallback : BottomSheetBehavior.BottomSheetCallback() {

    private val onSlideActions: MutableList<OnSlideAction> = mutableListOf()
    private val onStateChangedActions: MutableList<OnStateChangedAction> = mutableListOf()

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
        onSlideActions.forEach { it.onSlide(bottomSheet, slideOffset) }
    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        onStateChangedActions.forEach { it.onStateChanged(bottomSheet, newState) }
    }

    fun addOnSlideAction(action: OnSlideAction): Boolean {
        return onSlideActions.add(action)
    }

    fun removeOnSlideAction(action: OnSlideAction): Boolean {
        return onSlideActions.remove(action)
    }

    fun addOnStateChangedAction(action: OnStateChangedAction): Boolean {
        return onStateChangedActions.add(action)
    }

    fun addOnStateChangedAction(action: (newState: Int) -> Unit): Boolean {
        return addOnStateChangedAction(object : OnStateChangedAction {
            override fun onStateChanged(sheet: View, newState: Int) {
                action(newState)
            }
        })
    }

    fun removeOnStateChangedAction(action: OnStateChangedAction): Boolean {
        return onStateChangedActions.remove(action)
    }

    fun clear() {
        onSlideActions.clear()
        onStateChangedActions.clear()
    }
}
