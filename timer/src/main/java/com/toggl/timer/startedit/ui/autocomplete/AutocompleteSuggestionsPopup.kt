package com.toggl.timer.startedit.ui.autocomplete

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import com.toggl.common.ui.Position
import com.toggl.common.ui.RecyclerViewPopup
import com.toggl.models.common.AutocompleteSuggestion
import com.toggl.timer.R
import java.lang.ref.WeakReference
import kotlin.math.min

class AutocompleteSuggestionsPopup(
    context: Context,
    private val anchorTextView: WeakReference<TextView>,
    onSuggestionTapped: (AutocompleteSuggestion) -> Unit = {}
) {
    private val itemHeight = context.resources.getDimensionPixelSize(R.dimen.suggestions_popup_item_height)
    private val popupMaxHeight = itemHeight * 6
    private val popupHorizontalPadding = context.resources.getDimensionPixelSize(R.dimen.suggestions_popup_item_horizontal_padding)
    private val popup: RecyclerViewPopup<AutocompleteSuggestionViewHolder>
    private val adapter = AutocompleteSuggestionAdapter(onSuggestionTapped)

    init {
        popup = RecyclerViewPopup(
            context,
            anchorTextView.get()!!,
            R.layout.suggestions_popup,
            R.id.suggestions_recycler_view,
            adapter
        ).apply {
            inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
            isOutsideTouchable = true
            elevation = context.resources.getDimension(R.dimen.plane_06)
            setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.autocomplete_card_background))
        }
    }

    fun updateAutocompleteSuggestions(autocompleteSuggestions: List<AutocompleteSuggestionViewModel>) {
        adapter.submitList(autocompleteSuggestions)
    }

    fun show(screenWidth: Int, preferredPosition: Position, numberOfSuggestions: Int = adapter.itemCount) {
        anchorTextView.get()?.doOnLayout {
            if (numberOfSuggestions <= 0) {
                dismiss()
                return@doOnLayout
            }

            val locationOnScreen = intArrayOf(0, 0)
            it.getLocationOnScreen(locationOnScreen)
            val yPositionOfTextView = locationOnScreen[1]
            val popupHeight = min(numberOfSuggestions * itemHeight, popupMaxHeight)
            val actualMaxContentHeight = popup.safeGetMaxAvailableHeight(it)
            val actualHeight = min(popupHeight, actualMaxContentHeight)
            val popupYPosition = when (preferredPosition) {
                Position.Above -> yPositionOfTextView - actualHeight
                Position.Below -> yPositionOfTextView + it.height
            }

            popup.show(
                x = popupHorizontalPadding,
                y = popupYPosition,
                width = screenWidth - (popupHorizontalPadding * 2),
                height = actualHeight
            )
        }
    }

    fun dismiss() {
        popup.dismiss()
    }

    private fun PopupWindow.safeGetMaxAvailableHeight(view: View) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) getMaxAvailableHeight(view, 0, false)
        else popup.getMaxAvailableHeight(view, 0)
}