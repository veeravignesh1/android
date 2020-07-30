package com.toggl.timer.project.ui

import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.core.view.isVisible
import java.lang.ref.WeakReference

class ColorPickerAnimator(
    private val window: WeakReference<Window>,
    private val colorPickerContainer: WeakReference<View>,
    private val premiumPickerElements: WeakReference<View>,
    private val basicPickerHeight: Int,
    private val premiumPickerHeight: Int
) : Animation.AnimationListener {

    private var callback: (() -> Unit)? = null

    private val sceneRoot: ViewGroup? by lazy(LazyThreadSafetyMode.NONE) {
        window.get()?.run { decorView.findViewById<View>(Window.ID_ANDROID_CONTENT)?.parent as? ViewGroup }
    }

    init {
        window.get()?.run {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            decorView.setOnApplyWindowInsetsListener(
                View.OnApplyWindowInsetsListener { view, insets ->
                    sceneRoot?.let { TransitionManager.beginDelayedTransition(it, ChangeBounds()) }
                    return@OnApplyWindowInsetsListener view.onApplyWindowInsets(insets)
                }
            )
        }
    }

    fun finish() {
        callback = null
        window.get()?.run { decorView.setOnApplyWindowInsetsListener(null) }
    }

    fun showPicker(premium: Boolean, callback: () -> Unit) {
        colorPickerContainer.get()?.let { container ->
            premiumPickerElements.get()?.let { premiumElements ->
                this.callback = callback
                premiumElements.isVisible = premium
                premiumElements.post {
                    animate(container, if (premium) premiumPickerHeight else basicPickerHeight)
                }
            }
        }
    }

    fun hidePicker() {
        colorPickerContainer.get()?.let {
            animate(it, 0)
        }
    }

    private fun animate(picker: View, height: Int) {
        val animation = ResizeAnimation(picker, height)
        animation.setAnimationListener(this)
        picker.startAnimation(animation)
    }

    override fun onAnimationEnd(animation: Animation?) {
        callback?.invoke()
        callback = null
    }

    override fun onAnimationStart(animation: Animation?) {
    }

    override fun onAnimationRepeat(animation: Animation?) {
    }

    private class ResizeAnimation(var view: View, newHeight: Int) : Animation() {
        private var startingHeight: Int = view.layoutParams.height
        private var endHeight: Int = newHeight
        private var diff: Int = endHeight - startingHeight

        init {
            duration = 200
        }

        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            view.layoutParams.height = startingHeight + (diff * interpolatedTime).toInt()
            view.requestLayout()
        }

        override fun willChangeBounds() = true
    }
}
