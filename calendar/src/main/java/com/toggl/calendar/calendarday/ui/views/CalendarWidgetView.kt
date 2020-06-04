package com.toggl.calendar.calendarday.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.toggl.calendar.R
import com.toggl.calendar.common.domain.CalendarItem
import com.toggl.calendar.common.domain.SelectedCalendarItem
import com.toggl.environment.services.calendar.CalendarEvent
import java.time.Duration
import java.time.OffsetDateTime

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
    private var lineColor: Int
    private var calendarHourLinesHeight: Float
    private var calendarHourTextColor: Int
    private var calendarHoursTextSize: Float
    private var calendarEditingHandleTouchExtraMargins: Float
    private var editingHourLabelTextColor: Int
    private var editingHoursLabelPaintTextSize: Float
    private var editingHandlesHorizontalMargins: Float
    private var editingHandlesRadius: Float
    private var calendarEventsStartMargin: Float
    private var calendarEventsLeftPadding: Float
    private var calendarEventsRightPadding: Float
    private var calendarEventsItemsSpacing: Float
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
    private var calendarEventBottomLineHeight: Float
    private var calendarIconSize: Float
    private var calendarIconRightInsetMargin: Float
    private var calendarIconId: Int
    private var primaryTextColor: Int

    private val viewFrame = RectF()
    private var hourHeight: Float = 0f
    private var scrollOffset: Float = 0f

    var calendarItems: List<CalendarItem> = listOf(
        CalendarItem.CalendarEvent(
            CalendarEvent(
                id = "1",
                startTime = OffsetDateTime.now(),
                duration = Duration.ofMinutes(30),
                description = "Test Event",
                color = "#c2c2c2",
                calendarId = "1"
            ),
            0,
            1
        )
    )
    var selectedItem: SelectedCalendarItem? = null

    private val calendarItemDrawingDelegate = CalendarItemDrawingDelegate()
    private val backgroundDrawingDelegate = CalendarBackgroundDrawingDelegate()

    init {
        context.resources.run {
            val defaultCalendarBaseHourHeight = getDimension(R.dimen.default_calendar_calendarBaseHourHeight)
            val defaultCalendarMaxHourHeight = getDimension(R.dimen.default_calendar_calendarMaxHourHeight)
            val defaultCalendarEdgeDistanceToTriggerAutoScroll =
                getDimension(R.dimen.default_calendar_calendarEdgeDistanceToTriggerAutoScroll)
            val defaultCalendarEventAutoScrollToFrameExtraDistance =
                getDimension(R.dimen.default_calendar_calendarEventAutoScrollToFrameExtraDistance)
            val defaultCalendarCurrentHourCircleRadius = getDimension(R.dimen.default_calendar_calendarCurrentHourCircleRadius)
            val defaultCurrentHourColor = ContextCompat.getColor(context, R.color.default_calendar_currentHourColor)
            val defaultCalendarCurrentHourIndicatorStrokeSize =
                getDimension(R.dimen.default_calendar_calendarCurrentHourIndicatorStrokeSize)
            val defaultCalendarBackgroundColor =
                ContextCompat.getColor(context, R.color.default_calendar_calendarBackgroundColor)
            val defaultCalendarHourLineStartMargin = getDimension(R.dimen.default_calendar_calendarHourLineStartMargin)
            val defaultCalendarVerticalLineDividerStartMargin =
                getDimension(R.dimen.default_calendar_calendarVerticalLineDividerStartMargin)
            val defaultCalendarHoursStartMargin = getDimension(R.dimen.default_calendar_calendarHoursStartMargin)
            val defaultLineColor = ContextCompat.getColor(context, R.color.default_calendar_lineColor)
            val defaultCalendarHourLinesHeight = getDimension(R.dimen.default_calendar_calendarHourLinesHeight)
            val defaultCalendarHourTextColor = ContextCompat.getColor(context, R.color.default_calendar_calendarHourTextColor)
            val defaultCalendarHoursTextSize = getDimension(R.dimen.default_calendar_calendarHoursTextSize)
            val defaultCalendarEditingHandleTouchExtraMargins =
                getDimension(R.dimen.default_calendar_calendarEditingHandleTouchExtraMargins)
            val defaultEditingHourLabelTextColor =
                ContextCompat.getColor(context, R.color.default_calendar_editingHourLabelTextColor)
            val defaultEditingHoursLabelPaintTextSize = getDimension(R.dimen.default_calendar_editingHoursLabelPaintTextSize)
            val defaultEditingHandlesHorizontalMargins = getDimension(R.dimen.default_calendar_editingHandlesHorizontalMargins)
            val defaultEditingHandlesRadius = getDimension(R.dimen.default_calendar_editingHandlesRadius)
            val defaultCalendarEventsStartMargin = getDimension(R.dimen.default_calendar_calendarEventsStartMargin)
            val defaultCalendarEventsLeftPadding = getDimension(R.dimen.default_calendar_calendarEventsLeftPadding)
            val defaultCalendarEventsRightPadding = getDimension(R.dimen.default_calendar_calendarEventsRightPadding)
            val defaultCalendarEventsItemsSpacing = getDimension(R.dimen.default_calendar_calendarEventsItemsSpacing)
            val defaultShortCalendarItemHeight = getDimension(R.dimen.default_calendar_shortCalendarItemHeight)
            val defaultRegularCalendarItemVerticalPadding =
                getDimension(R.dimen.default_calendar_regularCalendarItemVerticalPadding)
            val defaultRegularCalendarItemHorizontalPadding =
                getDimension(R.dimen.default_calendar_regularCalendarItemHorizontalPadding)
            val defaultShortCalendarItemVerticalPadding =
                getDimension(R.dimen.default_calendar_shortCalendarItemVerticalPadding)
            val defaultShortCalendarItemHorizontalPadding =
                getDimension(R.dimen.default_calendar_shortCalendarItemHorizontalPadding)
            val defaultRegularCalendarItemFontSize = getDimension(R.dimen.default_calendar_regularCalendarItemFontSize)
            val defaultShortCalendarItemFontSize = getDimension(R.dimen.default_calendar_shortCalendarItemFontSize)
            val defaultTextEventsPaintTextSize = getDimension(R.dimen.default_calendar_textEventsPaintTextSize)
            val defaultCalendarRunningTimeEntryStripesSpacing =
                getDimension(R.dimen.default_calendar_calendarRunningTimeEntryStripesSpacing)
            val defaultCalendarRunningTimeEntryThinStripeWidth =
                getDimension(R.dimen.default_calendar_calendarRunningTimeEntryThinStripeWidth)
            val defaultCalendarRunningTimeEntryDashedHourTopPadding =
                getDimension(R.dimen.default_calendar_calendarRunningTimeEntryDashedHourTopPadding)
            val defaultCalendarEventBottomLineHeight = getDimension(R.dimen.default_calendar_calendarEventBottomLineHeight)
            val defaultCalendarIconSize = getDimension(R.dimen.default_calendar_calendarIconSize)
            val defaultCalendarIconRightInsetMargin = getDimension(R.dimen.default_calendar_calendarIconRightInsetMargin)
            val defaultCalendarIconId = R.drawable.ic_calendar
            val defaultPrimaryTextColor = ContextCompat.getColor(context, R.color.default_calendar_primaryTextColor)
            context.theme.obtainStyledAttributes(attrs, R.styleable.CalendarWidgetView, 0, 0).apply {
                try {
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
                        R.styleable.CalendarWidgetView_calendarHourLineStartMargin,
                        defaultCalendarHourLineStartMargin
                    )
                    calendarVerticalLineDividerStartMargin = getDimension(
                        R.styleable.CalendarWidgetView_calendarVerticalLineDividerStartMargin,
                        defaultCalendarVerticalLineDividerStartMargin
                    )
                    calendarHoursStartMargin =
                        getDimension(R.styleable.CalendarWidgetView_calendarHoursStartMargin, defaultCalendarHoursStartMargin)
                    lineColor = getColor(R.styleable.CalendarWidgetView_lineColor, defaultLineColor)
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
                        R.styleable.CalendarWidgetView_editingHoursLabelPaintTextSize,
                        defaultEditingHoursLabelPaintTextSize
                    )
                    editingHandlesHorizontalMargins = getDimension(
                        R.styleable.CalendarWidgetView_editingHandlesHorizontalMargins,
                        defaultEditingHandlesHorizontalMargins
                    )
                    editingHandlesRadius =
                        getDimension(R.styleable.CalendarWidgetView_editingHandlesRadius, defaultEditingHandlesRadius)
                    calendarEventsStartMargin =
                        getDimension(
                            R.styleable.CalendarWidgetView_calendarEventsStartMargin,
                            defaultCalendarEventsStartMargin
                        )
                    calendarEventsLeftPadding =
                        getDimension(
                            R.styleable.CalendarWidgetView_calendarEventsLeftPadding,
                            defaultCalendarEventsLeftPadding
                        )
                    calendarEventsRightPadding =
                        getDimension(
                            R.styleable.CalendarWidgetView_calendarEventsRightPadding,
                            defaultCalendarEventsRightPadding
                        )
                    calendarEventsItemsSpacing =
                        getDimension(
                            R.styleable.CalendarWidgetView_calendarEventsItemsSpacing,
                            defaultCalendarEventsItemsSpacing
                        )
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
                        R.styleable.CalendarWidgetView_shortCalendarItemVerticalPadding,
                        defaultShortCalendarItemVerticalPadding
                    )
                    shortCalendarItemHorizontalPadding = getDimension(
                        R.styleable.CalendarWidgetView_shortCalendarItemHorizontalPadding,
                        defaultShortCalendarItemHorizontalPadding
                    )
                    regularCalendarItemFontSize = getDimension(
                        R.styleable.CalendarWidgetView_regularCalendarItemFontSize,
                        defaultRegularCalendarItemFontSize
                    )
                    shortCalendarItemFontSize =
                        getDimension(
                            R.styleable.CalendarWidgetView_shortCalendarItemFontSize,
                            defaultShortCalendarItemFontSize
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
                } finally {
                    recycle()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        viewFrame.set(0f, scrollOffset, width.toFloat(), scrollOffset + height)

        canvas.save()
        canvas.translate(0f, -scrollOffset)
        canvas.clipRect(viewFrame)

        drawCalendarBackground(canvas)
        drawCalendarItems(canvas)
        drawCurrentHourIndicator()

        canvas.restore()
    }

    private fun drawCalendarBackground(canvas: Canvas) {
        backgroundDrawingDelegate.onDraw(canvas, viewFrame)
    }

    private fun drawCalendarItems(canvas: Canvas) {
        calendarItems.forEach { calendarItemDrawingDelegate.onDraw(canvas, viewFrame, it) }
    }

    private fun drawCurrentHourIndicator() {
    }
}