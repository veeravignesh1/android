package com.toggl.common.ui

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewPopup<VH : RecyclerView.ViewHolder>(
    context: Context,
    private val anchor: View,
    @LayoutRes layoutId: Int,
    @IdRes recyclerViewId: Int,
    adapter: RecyclerView.Adapter<VH>
) : PopupWindow(context) {

    private val recyclerView: RecyclerView

    init {
        val contentView = LayoutInflater.from(anchor.context).inflate(layoutId, null)
        recyclerView = contentView.findViewById(recyclerViewId)
        recyclerView.adapter = adapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            windowLayoutType = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL
        }

        this.contentView = contentView
    }

    fun show(x: Int, y: Int, width: Int, height: Int) {
        if (!isShowing) {
            showAtLocation(anchor, Gravity.NO_GRAVITY, x, y)
        }

        update(x, y, width, height)
    }
}