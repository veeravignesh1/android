package com.toggl.reports.ui.composables

import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Canvas
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.isSystemInDarkTheme
import androidx.ui.graphics.Color
import androidx.ui.graphics.drawscope.drawCanvas
import androidx.ui.graphics.toArgb
import androidx.ui.layout.Column
import androidx.ui.layout.aspectRatio
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.MaterialTheme
import androidx.ui.res.dimensionResource
import androidx.ui.unit.sp
import androidx.ui.util.toRadians
import com.toggl.common.extensions.adjustForUserTheme
import com.toggl.common.feature.compose.theme.grid_3
import com.toggl.reports.R
import com.toggl.reports.domain.DonutChartSegment
import kotlin.math.cos
import kotlin.math.sin

private const val innerCircleFactor = 0.6F
private const val minimumSegmentPercentageToShowLabel = 0.04F
private const val sliceLabelCenterRadius = (1 + innerCircleFactor) / 2

@Composable
fun DonutChart(segments: List<DonutChartSegment>) {

    Column {
        GroupHeader(text = "Projects")

        Box(gravity = ContentGravity.Center) {
            val innerCircleColor = MaterialTheme.colors.background
            val canvasModifier = Modifier.fillMaxWidth() +
                Modifier.aspectRatio(1F) +
                Modifier.padding(grid_3)

            val isInDarkTheme = isSystemInDarkTheme()
            val bounds = remember { Rect() }
            val textPaint = remember { Paint() }
            textPaint.textSize = 11.sp.value
            textPaint.color = MaterialTheme.colors.surface.toArgb()


            Canvas(modifier = canvasModifier) {
                //save()

                val innerCircleRadius = this.size.width * innerCircleFactor / 2

                for (segment in segments) {

                    drawArc(
                        Color(segment.color.adjustForUserTheme(isInDarkTheme)),
                        segment.startAngle,
                        segment.sweepAngle,
                        true
                    )

                    if (segment.percentage < minimumSegmentPercentageToShowLabel)
                        continue

                    val labelAngle = (segment.startAngle + segment.sweepAngle / 2).toRadians()

                    val x = center.x * (1 + sliceLabelCenterRadius * cos(labelAngle))
                    val y = center.y * (1 + sliceLabelCenterRadius * sin(labelAngle))

                    val text = String.format("%.2f%%", segment.percentage * 100)
                    drawCanvas { canvas, _ ->
                        textPaint.getTextBounds(text, 0, text.length, bounds)
                        val offsetY = bounds.height() / 2
                        canvas.nativeCanvas.drawText(text, x, y + offsetY, textPaint)
                    }
                }

                drawCircle(innerCircleColor, innerCircleRadius)
            }
        }
    }
}