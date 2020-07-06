package com.toggl.timer.project.ui

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewPropertyAnimator
import android.widget.EditText
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.math.max

class AutocompleteSuggestionsRevealAnimator(
    editTextToReveal: EditText,
    cancelButton: View,
    projectColorIndicator: View,
    chipsContainer: View,
    lifecycleOwner: LifecycleOwner,
    private var onRevealStartCallback: (() -> Unit)?
) {
    private var editTextToReveal: EditText? = editTextToReveal
    private var cancelButton: View? = cancelButton
    private var projectColorIndicator: View? = projectColorIndicator
    private var chipsContainer: View? = chipsContainer
    private var runningRevealAnimator: Animator? = null
    private var chipsContainerAnimator: ViewPropertyAnimator? = null
    private var projectColorIndicatorAnimator: ViewPropertyAnimator? = null

    init {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                clearRefs()
                clearRunningAnimators()
            }
        })
    }

    fun revealEditText() {
        val editText = editTextToReveal ?: return
        val cancelButton = cancelButton ?: return
        val projectColorIndicator = projectColorIndicator ?: return
        val chipsContainer = chipsContainer ?: return
        clearRunningAnimators()

        val cx = editText.measuredWidth / 2f
        val cy = editText.measuredHeight / 2f
        val finalRadius = max(editText.height, editText.width) / 2f

        val anim = ViewAnimationUtils.createCircularReveal(
            editText,
            cx.toInt(),
            cy.toInt(),
            0f,
            finalRadius
        ).setDuration(200)

        anim.doOnStart {
            onRevealStartCallback?.invoke()
        }
        anim.doOnEnd {
            editText.requestFocus()
        }

        editText.visibility = View.VISIBLE
        cancelButton.visibility = View.VISIBLE

        chipsContainerAnimator = chipsContainer.animate().alpha(0f)
            .setDuration(100)
            .withEndAction {
                chipsContainer.visibility = View.INVISIBLE
            }

        projectColorIndicatorAnimator = projectColorIndicator.animate().alpha(0f)
            .setDuration(100)
            .withEndAction {
                projectColorIndicator.visibility = View.INVISIBLE
            }
        runningRevealAnimator = anim
        anim.start()
    }

    fun hideEditText() {
        val editText = editTextToReveal ?: return
        val cancelButton = cancelButton ?: return
        val projectColorIndicator = projectColorIndicator ?: return
        val chipsContainer = chipsContainer ?: return
        clearRunningAnimators()

        val cx = editText.measuredWidth / 2f
        val cy = editText.measuredHeight / 2f
        val initialRadius = max(editText.height, editText.width) / 2f
        if (editText.isAttachedToWindow) {
            val anim = ViewAnimationUtils.createCircularReveal(
                editText,
                cx.toInt(),
                cy.toInt(),
                initialRadius,
                0f
            )
            anim.doOnEnd {
                editText.visibility = View.INVISIBLE
            }
            cancelButton.visibility = View.INVISIBLE
            chipsContainer.visibility = View.VISIBLE
            projectColorIndicator.visibility = View.VISIBLE
            chipsContainerAnimator = chipsContainer.animate()
                .alpha(1f)
                .setDuration(100)
                .setListener(null)
            projectColorIndicatorAnimator = projectColorIndicator.animate()
                .alpha(1f)
                .setDuration(100)
                .setListener(null)
            runningRevealAnimator = anim
            anim.start()
        } else {
            editText.visibility = View.INVISIBLE
            cancelButton.visibility = View.INVISIBLE
            chipsContainer.visibility = View.VISIBLE
            projectColorIndicator.visibility = View.VISIBLE
        }
    }

    private fun clearRunningAnimators() {
        runningRevealAnimator?.cancelAndRemoveListeners()
        chipsContainerAnimator?.cancelAndRemoveListeners()
        projectColorIndicatorAnimator?.cancelAndRemoveListeners()
        runningRevealAnimator = null
        chipsContainerAnimator = null
        projectColorIndicatorAnimator = null
    }

    private fun clearRefs() {
        editTextToReveal = null
        cancelButton = null
        projectColorIndicator = null
        chipsContainer = null
        onRevealStartCallback = null
    }

    private fun Animator.cancelAndRemoveListeners() {
        cancel()
        removeAllListeners()
    }

    private fun ViewPropertyAnimator.cancelAndRemoveListeners() {
        cancel()
        setListener(null)
    }
}
