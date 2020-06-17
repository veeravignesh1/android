package com.toggl.timer.startedit.ui.autocomplete

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.toggl.common.extensions.doSafeAfterTextChanged
import com.toggl.common.extensions.setSafeText
import com.toggl.timer.startedit.domain.StartEditAction.DescriptionEntered
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow

@FlowPreview
@ExperimentalCoroutinesApi
class AutocompleteTextInputEditText : TextInputEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var shouldEmit = true
    private var descriptionChangeListener: TextWatcher? = null
    // HACK: onSelectionChanged runs before the constructor, so this needs to be nullable
    private val channel: ConflatedBroadcastChannel<DescriptionEntered>? = ConflatedBroadcastChannel()

    val onDescriptionChanged = channel!!.asFlow()

    init {
        descriptionChangeListener = doSafeAfterTextChanged {

            if (!shouldEmit)
                return@doSafeAfterTextChanged

            channel?.offer(DescriptionEntered(text.toString(), selectionEnd))
        }
    }

    fun clearDescriptionChangedListeners() {
        removeTextChangedListener(descriptionChangeListener)
    }

    fun setSafeTextPreventingActionDispatching(newText: String) {
        shouldEmit = false
        setSafeText(newText)
        shouldEmit = true
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)

        if (!shouldEmit)
            return

        channel?.offer(DescriptionEntered(text.toString(), selectionEnd))
    }
}