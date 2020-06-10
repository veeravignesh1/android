package com.toggl.calendar.calendarday.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.withTranslation
import com.toggl.calendar.R
import com.toggl.calendar.common.domain.CalendarItem
import com.toggl.common.Constants.ClockMath.hoursInTheDay
import com.toggl.common.extensions.applyAndRecycle
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class CalendarWidgetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var currentHourColor: Int
    private var calendarCurrentHourCircleRadius: Float
    private var calendarEventAutoScrollToFrameExtraDistance: Float
    private var calendarEdgeDistanceToTriggerAutoScroll: Float
    private var calendarMaxHourHeight: Float
    private var calendarBaseHourHeight: Float
    private var calendarCurrentHourIndicatorStrokeSize: Float
    private var calendarBackgroundColor: Int
    private var calendarHourLineStartMargin: Float
    private var calendarVerticalLineDividerStartMargin: Float
    private var calendarHoursStartMargin: Float
    private var calendarBackgroundHourLineColor: Int
    private var calendarHourLinesHeight: Float
    private var calendarHourTextColor: Int
    private var calendarHoursTextSize: Float
    private var calendarEditingHandleTouchExtraMargins: Float
    private var editingHourLabelTextColor: Int
    private var editingHoursLabelPaintTextSize: Float
    private var editingHandlesHorizontalMargins: Float
    private var editingHandlesRadius: Float
    private var editingHandlesStrokeWidth: Float
    private var calendarEventsStartMargin: Float
    private var calendarEventsLeftPadding: Float
    private var calendarEventsRightPadding: Float
    private var calendarEventsItemsSpacing: Float
    private var calendarEventsCornerRadius: Float
    private var shortCalendarItemHeight: Float
    private var regularCalendarItemVerticalPadding: Float
    private var regularCalendarItemHorizontalPadding: Float
    private var shortCalendarItemVerticalPadding: Float
    private var shortCalendarItemHorizontalPadding: Float
    private var regularCalendarItemFontSize: Float
    private var shortCalendarItemFontSize: Float
    private var textEventsPaintTextSize: Float
    private var calendarRunningTimeEntryStripesSpacing: Float
    private var calendarRunningTimeEntryThinStripeWidth: Float
    private var calendarRunningTimeEntryDashedHourTopPadding: Float
    private var calendarRunningTimeEntryBorderStrokeWidth: Float
    private var calendarRunningTimeEntryExtraHeight: Float
    private var calendarEventBottomLineHeight: Float
    private var calendarIconSize: Float
    private var calendarIconRightInsetMargin: Float
    private var calendarIconId: Int
    private var primaryTextColor: Int
    private var defaultCalendarItemColor: Int

    private val viewFrame = RectF()
    private var hourHeight: Float = 0f
    private var scrollOffset: Float = 500f

    private var drawingLock = ReentrantLock(true)
    private var drawingData: CalendarWidgetViewDrawingData = CalendarWidgetViewDrawingData()
    private var isDragging: Boolean = false
    private var flingWasCalled: Boolean = false
    private var isScrolling: Boolean = false

    private val scroller: OverScroller
    private val gestureDetector: GestureDetector
    private val scaleGestureDetector: ScaleGestureDetector

    private val calendarItemDrawingDelegate: CalendarItemDrawingDelegate
    private val backgroundDrawingDelegate: CalendarBackgroundDrawingDelegate

    init {
        context.resources.run {
            val defaultCalendarBaseHourHeight = getDimension(R.dimen.default_calendar_calendar_base_hour_height)
            val defaultCalendarMaxHourHeight = getDimension(R.dimen.default_calendar_calendar_max_hour_height)
            val defaultCalendarEdgeDistanceToTriggerAutoScroll =
                getDimension(R.dimen.default_calendar_calendar_edge_distance_to_trigger_auto_scroll)
            val defaultCalendarEventAutoScrollToFrameExtraDistance =
                getDimension(R.dimen.default_calendar_calendar_event_auto_scroll_to_frame_extra_distance)
            val defaultCalendarCurrentHourCircleRadius =
                getDimension(R.dimen.default_calendar_calendar_current_hour_circle_radius)
            val defaultCurrentHourColor = ContextCompat.getColor(context, R.color.default_calendar_current_hour_color)
            val defaultCalendarCurrentHourIndicatorStrokeSize =
                getDimension(R.dimen.default_calendar_calendar_current_hour_indicator_stroke_size)
            val defaultCalendarBackgroundColor =
                ContextCompat.getColor(context, R.color.default_calendar_calendar_background_color)
            val defaultCalendarHourLineStartMargin = getDimension(R.dimen.default_calendar_calendar_hour_line_start_margin)
            val defaultCalendarVerticalLineDividerStartMargin =
                getDimension(R.dimen.default_calendar_calendar_vertical_line_divider_start_margin)
            val defaultCalendarHoursStartMargin = getDimension(R.dimen.default_calendar_calendar_hours_start_margin)
            val defaultCalendarHourLineColor =
                ContextCompat.getColor(context, R.color.default_calendar_calendar_hour_line_color)
            val defaultCalendarHourLinesHeight = getDimension(R.dimen.default_calendar_calendar_hour_lines_height)
            val defaultCalendarHourTextColor =
                ContextCompat.getColor(context, R.color.default_calendar_calendar_hour_text_color)
            val defaultCalendarHoursTextSize = getDimension(R.dimen.default_calendar_calendar_hours_text_size)
            val defaultCalendarEditingHandleTouchExtraMargins =
                getDimension(R.dimen.default_calendar_calendar_editing_handle_touch_extra_margins)
            val defaultEditingHourLabelTextColor =
                ContextCompat.getColor(context, R.color.default_calendar_editing_hour_label_text_color)
            val defaultEditingHoursLabelPaintTextSize =
                getDimension(R.dimen.default_calendar_editing_hours_label_paint_text_size)
            val defaultEditingHandlesHorizontalMargins =
                getDimension(R.dimen.default_calendar_editing_handles_horizontal_margins)
            val defaultEditingHandlesRadius = getDimension(R.dimen.default_calendar_editing_handles_radius)
            val defaultEditingHandlesStrokeWidth = getDimension(R.dimen.default_calendar_editing_handles_stroke_width)
            val defaultCalendarEventsStartMargin = getDimension(R.dimen.default_calendar_calendar_events_start_margin)
            val defaultCalendarEventsLeftPadding = getDimension(R.dimen.default_calendar_calendar_events_left_padding)
            val defaultCalendarEventsRightPadding = getDimension(R.dimen.default_calendar_calendar_events_right_padding)
            val defaultCalendarEventsItemsSpacing = getDimension(R.dimen.default_calendar_calendar_events_items_spacing)
            val defaultCalendarEventsCornerRadius = getDimension(R.dimen.default_calendar_calendar_events_corner_radius)
            val defaultShortCalendarItemHeight = getDimension(R.dimen.default_calendar_short_calendar_item_height)
            val defaultRegularCalendarItemVerticalPadding =
                getDimension(R.dimen.default_calendar_regular_calendar_item_vertical_padding)
            val defaultRegularCalendarItemHorizontalPadding =
                getDimension(R.dimen.default_calendar_regular_calendar_item_horizontal_padding)
            val defaultShortCalendarItemVerticalPadding =
                getDimension(R.dimen.default_calendar_short_calendar_item_vertical_padding)
            val defaultShortCalendarItemHorizontalPadding =
                getDimension(R.dimen.default_calendar_short_calendar_item_horizontal_padding)
            val defaultRegularCalendarItemFontSize = getDimension(R.dimen.default_calendar_regular_calendar_item_font_size)
            val defaultShortCalendarItemFontSize = getDimension(R.dimen.default_calendar_short_calendar_item_font_size)
            val defaultTextEventsPaintTextSize = getDimension(R.dimen.default_calendar_text_events_paint_text_size)
            val defaultCalendarRunningTimeEntryStripesSpacing =
                getDimension(R.dimen.default_calendar_calendar_running_time_entry_stripes_spacing)
            val defaultCalendarRunningTimeEntryThinStripeWidth =
                getDimension(R.dimen.default_calendar_calendar_running_time_entry_thin_stripe_width)
            val defaultCalendarRunningTimeEntryDashedHourTopPadding =
                getDimension(R.dimen.default_calendar_calendar_running_time_entry_dashed_hour_top_padding)
            val defaultCalendarRunningTimeEntryBorderStrokeWidth =
                getDimension(R.dimen.default_calendar_calendar_running_time_entry_border_stroke_width)
            val defaultCalendarRunningTimeEntryExtraHeight =
                getDimension(R.dimen.default_calendar_calendar_running_time_entry_extra_height)
            val defaultCalendarEventBottomLineHeight = getDimension(R.dimen.default_calendar_calendar_event_bottom_line_height)
            val defaultCalendarIconSize = getDimension(R.dimen.default_calendar_calendar_icon_size)
            val defaultCalendarIconRightInsetMargin = getDimension(R.dimen.default_calendar_calendarIcon_right_inset_margin)
            val defaultCalendarIconId = R.drawable.ic_calendar
            val defaultPrimaryTextColor = ContextCompat.getColor(context, R.color.default_calendar_primary_text_color)
            val defaultDefaultCalendarItemColor =
                ContextCompat.getColor(context, R.color.default_calendar_default_calendar_item_color)
            context.theme.obtainStyledAttributes(attrs, R.styleable.CalendarWidgetView, 0, 0).applyAndRecycle {
                calendarBaseHourHeight =
                    getDimension(R.styleable.CalendarWidgetView_calendarBaseHourHeight, defaultCalendarBaseHourHeight)
                calendarMaxHourHeight =
                    getDimension(R.styleable.CalendarWidgetView_calendarMaxHourHeight, defaultCalendarMaxHourHeight)
                calendarEdgeDistanceToTriggerAutoScroll = getDimension(
                    R.styleable.CalendarWidgetView_calendarEdgeDistanceToTriggerAutoScroll,
                    defaultCalendarEdgeDistanceToTriggerAutoScroll
                )
                calendarEventAutoScrollToFrameExtraDistance = getDimension(
                    R.styleable.CalendarWidgetView_calendarEventAutoScrollToFrameExtraDistance,
                    defaultCalendarEventAutoScrollToFrameExtraDistance
                )
                calendarCurrentHourCircleRadius = getDimension(
                    R.styleable.CalendarWidgetView_calendarCurrentHourCircleRadius,
                    defaultCalendarCurrentHourCircleRadius
                )
                currentHourColor = getColor(R.styleable.CalendarWidgetView_currentHourColor, defaultCurrentHourColor)
                calendarCurrentHourIndicatorStrokeSize = getDimension(
                    R.styleable.CalendarWidgetView_calendarCurrentHourIndicatorStrokeSize,
                    defaultCalendarCurrentHourIndicatorStrokeSize
                )
                calendarBackgroundColor =
                    getColor(R.styleable.CalendarWidgetView_calendarBackgroundColor, defaultCalendarBackgroundColor)
                calendarHourLineStartMargin = getDimension(
                    R.styleable.CalendarWidgetView_calendarHourLineStartMargin, defaultCalendarHourLineStartMargin
                )
                calendarVerticalLineDividerStartMargin = getDimension(
                    R.styleable.CalendarWidgetView_calendarVerticalLineDividerStartMargin,
                    defaultCalendarVerticalLineDividerStartMargin
                )
                calendarHoursStartMargin =
                    getDimension(R.styleable.CalendarWidgetView_calendarHoursStartMargin, defaultCalendarHoursStartMargin)
                calendarBackgroundHourLineColor =
                    getColor(R.styleable.CalendarWidgetView_calendarHourLineColor, defaultCalendarHourLineColor)
                calendarHourLinesHeight =
                    getDimension(R.styleable.CalendarWidgetView_calendarHourLinesHeight, defaultCalendarHourLinesHeight)
                calendarHourTextColor =
                    getColor(R.styleable.CalendarWidgetView_calendarHourTextColor, defaultCalendarHourTextColor)
                calendarHoursTextSize =
                    getDimension(R.styleable.CalendarWidgetView_calendarHoursTextSize, defaultCalendarHoursTextSize)
                calendarEditingHandleTouchExtraMargins = getDimension(
                    R.styleable.CalendarWidgetView_calendarEditingHandleTouchExtraMargins,
                    defaultCalendarEditingHandleTouchExtraMargins
                )
                editingHourLabelTextColor =
                    getColor(R.styleable.CalendarWidgetView_editingHourLabelTextColor, defaultEditingHourLabelTextColor)
                editingHoursLabelPaintTextSize = getDimension(
                    R.styleable.CalendarWidgetView_editingHoursLabelPaintTextSize, defaultEditingHoursLabelPaintTextSize
                )
                editingHandlesHorizontalMargins =
                    getDimension(
                        R.styleable.CalendarWidgetView_editingHandlesHorizontalMargins, defaultEditingHandlesHorizontalMargins
                    )
                editingHandlesRadius =
                    getDimension(R.styleable.CalendarWidgetView_editingHandlesRadius, defaultEditingHandlesRadius)
                editingHandlesStrokeWidth =
                    getDimension(R.styleable.CalendarWidgetView_editingHandlesStrokeWidth, defaultEditingHandlesStrokeWidth)
                calendarEventsStartMargin =
                    getDimension(R.styleable.CalendarWidgetView_calendarEventsStartMargin, defaultCalendarEventsStartMargin)
                calendarEventsLeftPadding =
                    getDimension(R.styleable.CalendarWidgetView_calendarEventsLeftPadding, defaultCalendarEventsLeftPadding)
                calendarEventsRightPadding =
                    getDimension(R.styleable.CalendarWidgetView_calendarEventsRightPadding, defaultCalendarEventsRightPadding)
                calendarEventsItemsSpacing =
                    getDimension(R.styleable.CalendarWidgetView_calendarEventsItemsSpacing, defaultCalendarEventsItemsSpacing)
                calendarEventsCornerRadius =
                    getDimension(R.styleable.CalendarWidgetView_calendarEventsCornerRadius, defaultCalendarEventsCornerRadius)
                shortCalendarItemHeight =
                    getDimension(R.styleable.CalendarWidgetView_shortCalendarItemHeight, defaultShortCalendarItemHeight)
                regularCalendarItemVerticalPadding = getDimension(
                    R.styleable.CalendarWidgetView_regularCalendarItemVerticalPadding,
                    defaultRegularCalendarItemVerticalPadding
                )
                regularCalendarItemHorizontalPadding = getDimension(
                    R.styleable.CalendarWidgetView_regularCalendarItemHorizontalPadding,
                    defaultRegularCalendarItemHorizontalPadding
                )
                shortCalendarItemVerticalPadding = getDimension(
                    R.styleable.CalendarWidgetView_shortCalendarItemVerticalPadding, defaultShortCalendarItemVerticalPadding
                )
                shortCalendarItemHorizontalPadding = getDimension(
                    R.styleable.CalendarWidgetView_shortCalendarItemHorizontalPadding,
                    defaultShortCalendarItemHorizontalPadding
                )
                regularCalendarItemFontSize = getDimension(
                    R.styleable.CalendarWidgetView_regularCalendarItemFontSize, defaultRegularCalendarItemFontSize
                )
                shortCalendarItemFontSize = getDimension(
                    R.styleable.CalendarWidgetView_shortCalendarItemFontSize, defaultShortCalendarItemFontSize
                )
                textEventsPaintTextSize =
                    getDimension(R.styleable.CalendarWidgetView_textEventsPaintTextSize, defaultTextEventsPaintTextSize)
                calendarRunningTimeEntryStripesSpacing = getDimension(
                    R.styleable.CalendarWidgetView_calendarRunningTimeEntryStripesSpacing,
                    defaultCalendarRunningTimeEntryStripesSpacing
                )
                calendarRunningTimeEntryThinStripeWidth = getDimension(
                    R.styleable.CalendarWidgetView_calendarRunningTimeEntryThinStripeWidth,
                    defaultCalendarRunningTimeEntryThinStripeWidth
                )
                calendarRunningTimeEntryDashedHourTopPadding = getDimension(
                    R.styleable.CalendarWidgetView_calendarRunningTimeEntryDashedHourTopPadding,
                    defaultCalendarRunningTimeEntryDashedHourTopPadding
                )
                calendarRunningTimeEntryBorderStrokeWidth = getDimension(
                    R.styleable.CalendarWidgetView_calendarRunningTimeEntryBorderStrokeWidth,
                    defaultCalendarRunningTimeEntryBorderStrokeWidth
                )
                calendarRunningTimeEntryExtraHeight = getDimension(
                    R.styleable.CalendarWidgetView_calendarRunningTimeEntryExtraHeight,
                    defaultCalendarRunningTimeEntryExtraHeight
                )
                calendarEventBottomLineHeight = getDimension(
                    R.styleable.CalendarWidgetView_calendarEventBottomLineHeight,
                    defaultCalendarEventBottomLineHeight
                )
                calendarIconSize = getDimension(R.styleable.CalendarWidgetView_calendarIconSize, defaultCalendarIconSize)
                calendarIconRightInsetMargin = getDimension(
                    R.styleable.CalendarWidgetView_calendarIconRightInsetMargin,
                    defaultCalendarIconRightInsetMargin
                )
                calendarIconId = getResourceId(R.styleable.CalendarWidgetView_calendarIcon, defaultCalendarIconId)
                primaryTextColor = getColor(R.styleable.CalendarWidgetView_primaryTextColor, defaultPrimaryTextColor)
                defaultCalendarItemColor =
                    getColor(R.styleable.CalendarWidgetView_defaultCalendarItemColor, defaultDefaultCalendarItemColor)
            }
        }

        val normalCalendarIconBitmap =
            ContextCompat.getDrawable(context, calendarIconId)!!.toBitmap(calendarIconSize.toInt(), calendarIconSize.toInt())
        val smallCalendarIconBitmap =
            ContextCompat.getDrawable(context, calendarIconId)!!
                .toBitmap((calendarIconSize / 2f).toInt(), (calendarIconSize / 2).toInt())

        hourHeight = calendarBaseHourHeight
        calendarItemDrawingDelegate = CalendarItemDrawingDelegate(
            itemSpacing = calendarEventsItemsSpacing,
            leftMargin = calendarVerticalLineDividerStartMargin,
            leftPadding = calendarEventsLeftPadding,
            rightPadding = calendarEventsRightPadding,
            cornerRadius = calendarEventsCornerRadius,
            calendarEventBottomLineHeight = calendarEventBottomLineHeight,
            normalCalendarIconBitmap = normalCalendarIconBitmap,
            smallCalendarIconBitMap = smallCalendarIconBitmap,
            calendarBackgroundColor = calendarBackgroundColor,
            runningTimeEntryStripesSpacing = calendarRunningTimeEntryStripesSpacing,
            runningTimeEntryThinStripeWidth = calendarRunningTimeEntryThinStripeWidth,
            runningTimeEntryBorderStrokeWidth = calendarRunningTimeEntryBorderStrokeWidth,
            runningTimeEntryDashedHourTopPadding = calendarRunningTimeEntryDashedHourTopPadding,
            calendarRunningTimeEntryExtraHeight = calendarRunningTimeEntryExtraHeight,
            calendarIconRightInsetMargin = calendarIconRightInsetMargin,
            calendarIconSize = calendarIconSize,
            primaryTextColor = primaryTextColor,
            regularCalendarItemFontSize = regularCalendarItemFontSize,
            regularCalendarItemHorizontalPadding = regularCalendarItemHorizontalPadding,
            regularCalendarItemVerticalPadding = regularCalendarItemVerticalPadding,
            shortCalendarItemFontSize = shortCalendarItemFontSize,
            shortCalendarItemHeight = shortCalendarItemHeight,
            shortCalendarItemHorizontalPadding = shortCalendarItemHorizontalPadding,
            shortCalendarItemVerticalPadding = shortCalendarItemVerticalPadding,
            editingHandlesHorizontalMargins = editingHandlesHorizontalMargins,
            editingHandlesRadius = editingHandlesRadius,
            editingHandlesStrokeWidth = editingHandlesStrokeWidth,
            defaultCalendarItemColor = defaultCalendarItemColor
        ).apply {
            currentHourHeight = calendarBaseHourHeight
        }

        backgroundDrawingDelegate = CalendarBackgroundDrawingDelegate(
            hourLineColor = calendarBackgroundHourLineColor,
            hourLabelTextColor = calendarHourTextColor,
            hourLabelTextSize = calendarHoursTextSize,
            verticalLineLeftMargin = calendarVerticalLineDividerStartMargin,
            timeSliceStartX = calendarHourLineStartMargin,
            hoursX = calendarHoursStartMargin
        ).also {
            it.currentHourHeight = calendarBaseHourHeight
        }

        val gestureListener = GestureListener(
            ::onTouchDown,
            ::onLongPress,
            ::scrollView,
            ::flingView,
            ::onSingleTapUp,
            ::onScale
        )
        gestureDetector = GestureDetector(context, gestureListener)
        scaleGestureDetector = ScaleGestureDetector(context, gestureListener)
        scroller = OverScroller(context)
    }

    fun updateList(newCalendarItems: List<CalendarItem>) {
        drawingLock.withLock {
            drawingData = drawingData.withSourceItems(newCalendarItems)
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        viewFrame.set(0f, scrollOffset, width.toFloat(), scrollOffset + height)

        canvas.withTranslation(0f, -scrollOffset) {
            clipRect(viewFrame)
            drawCalendarBackground()
            drawCalendarItems()
            drawCurrentHourIndicator()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (!changed)
            return

        backgroundDrawingDelegate.onLayout()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val scaleResult = scaleGestureDetector.onTouchEvent(event)
        if (scaleGestureDetector.isInProgress)
            return scaleResult || super.onTouchEvent(event)

        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                gestureDetector.onTouchEvent(event)
                true
            }
            MotionEvent.ACTION_UP -> {
                isDragging = false
                gestureDetector.onTouchEvent(event)
                if (flingWasCalled)
                    return true
                if (!isScrolling)
                    return true

                isScrolling = false
                invalidate()
                true
            }
            MotionEvent.ACTION_MOVE -> {
                gestureDetector.onTouchEvent(event)
                if (isDragging) {
                    TODO("Drag event")
                }
                true
            }
            MotionEvent.ACTION_CANCEL -> {
                isDragging = false
                gestureDetector.onTouchEvent(event)
                isScrolling = false
                true
            }
            else -> gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
        }
    }

    private fun Canvas.drawCalendarBackground() {
        backgroundDrawingDelegate.onDraw(this, viewFrame)
    }

    private fun Canvas.drawCalendarItems() {
        drawingData.let { currentDrawingData ->
            val calendarItemsToDraw = currentDrawingData.nonSelectedCalendarItemsToDraw
            val selectedItemToDraw = currentDrawingData.selectedCalendarItemToDraw
            calendarItemsToDraw.forEach { calendarItemDrawingDelegate.onDraw(this, viewFrame, it, false) }
            selectedItemToDraw?.let { calendarItemDrawingDelegate.onDraw(this, viewFrame, it, true) }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun Canvas.drawCurrentHourIndicator() {
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onTouchDown(e: MotionEvent) {
        scroller.forceFinished(true)
        flingWasCalled = false
        handler.removeCallbacks(::continueScroll)
        invalidate()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onLongPress(e: MotionEvent) {
    }

    @Suppress("UNUSED_PARAMETER")
    private fun scrollView(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float) {
        if (isDragging)
            return

        val oldScrollOffset = scrollOffset
        val newScrollOffset = scrollOffset + distanceY
        scrollOffset = newScrollOffset.coerceIn(0f, calculateMaxHeight() - height)

        onScrollChanged(0, scrollOffset.toInt(), 0, oldScrollOffset.toInt())

        isScrolling = true
        postInvalidate()
    }

    private fun calculateMaxHeight() = hourHeight * hoursInTheDay

    @Suppress("UNUSED_PARAMETER")
    private fun flingView(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float) {
        scroller.forceFinished(true)
        flingWasCalled = true
        isScrolling = true
        scroller.fling(0, scrollOffset.toInt(), 0, (-velocityY / 2f).toInt(), 0, 0, 0, calculateMaxHeight().toInt())
        handler.post(::continueScroll)
    }

    private fun continueScroll() {
        isScrolling = isScrolling && scroller.computeScrollOffset()
        if (!isScrolling) {
            invalidate()
            return
        }

        if (scroller.currY == scrollOffset.toInt()) {
            handler.post(::continueScroll)
            return
        }

        val oldScrollOffset = scrollOffset
        scrollOffset = scroller.currY.coerceIn(0, (calculateMaxHeight() - height).toInt()).toFloat()

        onScrollChanged(0, scrollOffset.toInt(), 0, oldScrollOffset.toInt())

        handler.post(::continueScroll)
        invalidate()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onSingleTapUp(e: MotionEvent) {
    }

    private fun onScale(scaleDetector: ScaleGestureDetector): Boolean {
        val oldHourHeight = hourHeight
        val scaleFactor = scaleDetector.scaleFactor
        val newHourHeight = (hourHeight * scaleFactor)
        hourHeight = newHourHeight.coerceIn(calendarBaseHourHeight, calendarMaxHourHeight)

        val hourSizeChanged = oldHourHeight != hourHeight
        if (!hourSizeChanged)
            return true

        // Since the size of each hour is an integer
        // we first need to calculate the real scale
        // factor applied to the calendar
        val actualScale = (newHourHeight - oldHourHeight) / ((newHourHeight + oldHourHeight) / 2)

        // We need to calculate so the calendar feels
        // like it's zooming in and not sliding below
        // the user's fingers
        val focusPointOffset = scaleDetector.focusY * actualScale
        val scaledOffset = scrollOffset * actualScale
        val newScrollOffset = scrollOffset + scaledOffset + focusPointOffset
        scrollOffset = newScrollOffset.coerceIn(0f, calculateMaxHeight() - height)

        backgroundDrawingDelegate.currentHourHeight = hourHeight
        calendarItemDrawingDelegate.currentHourHeight = hourHeight
        invalidate()

        return true
    }

    inner class GestureListener(
        val onDownCallback: (MotionEvent) -> Unit,
        val onLongPressCallback: (MotionEvent) -> Unit,
        val onScrollCallback: (MotionEvent, MotionEvent, Float, Float) -> Unit,
        val onFlingCallback: (MotionEvent, MotionEvent, Float, Float) -> Unit,
        val onSingleTapUpCallback: (MotionEvent) -> Unit,
        val onScaleCallback: (ScaleGestureDetector) -> Boolean
    ) : GestureDetector.SimpleOnGestureListener(), ScaleGestureDetector.OnScaleGestureListener {

        override fun onDown(e: MotionEvent): Boolean {
            onDownCallback(e)
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            onFlingCallback(e1, e2, velocityX, velocityY)
            return true
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            onScrollCallback(e1, e2, distanceX, distanceY)
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            onLongPressCallback(e)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            onSingleTapUpCallback(e)
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean = onScaleCallback(detector)

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean = true

        override fun onScaleEnd(detector: ScaleGestureDetector) {}
    }
}