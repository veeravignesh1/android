package com.toggl.common.ui

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.toggl.common.android.R
import kotlin.math.min

class LifecycleAwareAutocompletePopup<ViewModel, ViewHolder : RecyclerView.ViewHolder>(
    context: Context,
    anchorTextView: AutocompleteTextInputEditText,
    adapter: ListAdapter<ViewModel, ViewHolder>,
    lifecycleOwner: LifecycleOwner
) {
    private val itemHeight = context.resources.getDimensionPixelSize(R.dimen.suggestions_popup_item_height)
    private val popupMaxHeight = itemHeight * 6
    private val popupHorizontalPadding =
        context.resources.getDimensionPixelSize(R.dimen.suggestions_popup_item_horizontal_padding)

    private var anchorTextView: AutocompleteTextInputEditText? = anchorTextView
    private var adapter: ListAdapter<ViewModel, ViewHolder>? = adapter

    private var popup: RecyclerViewPopup<ViewHolder>? = RecyclerViewPopup(
        context,
        anchorTextView,
        R.layout.suggestions_popup,
        R.id.suggestions_recycler_view,
        adapter
    ).apply {
        inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        isOutsideTouchable = true
        elevation = context.resources.getDimension(R.dimen.plane_06)
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.autocomplete_card_background))
    }

    init {
        lifecycleOwner.lifecycle.addObserver(LifecycleObserver())
    }

    var isShowing = false

    fun show(preferredPosition: Position, numberOfSuggestions: Int = adapter?.itemCount ?: 0) {
        anchorTextView?.doOnLayout { anchor ->
            val screenWidth = anchor.context.resources.displayMetrics.widthPixels
            if (numberOfSuggestions <= 0 || !isShowing) {
                dismiss()
                return@doOnLayout
            }
            popup?.run {
                val locationOnScreen = intArrayOf(0, 0)
                anchor.getLocationOnScreen(locationOnScreen)
                val yPositionOfTextView = locationOnScreen[1]
                val popupHeight = min(numberOfSuggestions * itemHeight, popupMaxHeight)
                val actualMaxContentHeight = safeGetMaxAvailableHeight(anchor)
                val actualHeight = min(popupHeight, actualMaxContentHeight)
                val popupYPosition = when (preferredPosition) {
                    Position.Above -> yPositionOfTextView - actualHeight
                    Position.Below -> yPositionOfTextView + anchor.height
                }

                show(
                    x = popupHorizontalPadding,
                    y = popupYPosition,
                    width = screenWidth - (popupHorizontalPadding * 2),
                    height = actualHeight
                )
            }
        }
    }

    fun dismiss() {
        popup?.dismiss()
    }

    private fun PopupWindow.safeGetMaxAvailableHeight(view: View) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) getMaxAvailableHeight(view, 0, false)
        else this.getMaxAvailableHeight(view, 0)

    private inner class LifecycleObserver : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            anchorTextView?.clearDescriptionChangedListeners()
            anchorTextView = null
            adapter = null
            popup = null
        }

        override fun onPause(owner: LifecycleOwner) {
            popup?.dismiss()
        }

        override fun onResume(owner: LifecycleOwner) {
            if (isShowing) {
                adapter?.run { show(Position.Above, itemCount) }
            }
        }
    }
}
