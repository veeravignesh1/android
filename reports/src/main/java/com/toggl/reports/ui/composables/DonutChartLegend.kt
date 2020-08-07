package com.toggl.reports.ui.composables

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.height
import androidx.ui.layout.padding
import androidx.ui.layout.width
import androidx.ui.material.Divider
import androidx.ui.text.font.FontWeight
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.toggl.common.feature.compose.ThemedPreview
import com.toggl.common.feature.compose.theme.adjustForUserTheme
import com.toggl.common.feature.compose.theme.grid_2
import com.toggl.reports.domain.DonutChartLegendInfo

@Composable
fun DonutChartLegend(legend: DonutChartLegendInfo) {
    Row(
        modifier = Modifier.padding(top = grid_2, bottom = grid_2),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalGravity = Alignment.CenterVertically
    ) {
        val projectColor = legend.projectColorHex.adjustForUserTheme()

        Box(
            shape = CircleShape,
            backgroundColor = projectColor,
            modifier = Modifier.height(10.dp) +
                       Modifier.width(10.dp)
        )

        Text(
            text = legend.projectName,
            modifier = Modifier.weight(8F, fill = true) +
                       Modifier.padding(start = grid_2)
        )

        Text(
            text = legend.percentage,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(2F)
        )

        Text(
            text = legend.duration,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(4F)
        )
    }

    Divider()
}

private val data1 = DonutChartLegendInfo(
    projectName = "Time management",
    projectColorHex = "#06AAF5",
    percentage = "80%",
    duration = "16:12:00"
)

private val data2 = DonutChartLegendInfo(
    "Important timesheets",
    "#EA468D",
    "15%",
    "9:00:00"
)

private val data3 = DonutChartLegendInfo(
    "Mobile apps",
    "#F1C33F",
    "5%",
    "7:12:00"
)

@Preview("DonutChartLegend light theme")
@Composable
fun PreviewDonutChartLegendLight() {
    ThemedPreview {
        Column {
            DonutChartLegend(data1)
            DonutChartLegend(data2)
            DonutChartLegend(data3)
        }
    }
}

@Preview("DonutChartLegend dark theme")
@Composable
fun PreviewDonutChartLegendDark() {
    ThemedPreview(darkTheme = true) {
        Column {
            DonutChartLegend(data1)
            DonutChartLegend(data2)
            DonutChartLegend(data3)
        }
    }
}

