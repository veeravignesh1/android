package com.toggl.reports.ui.composables

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.padding
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.toggl.common.feature.compose.ThemedPreview
import com.toggl.common.feature.compose.theme.grid_2
import com.toggl.reports.domain.BillableData

@Composable
fun Billable(billableData: BillableData) {

    Column {
        SectionHeader(text = "Billable")
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(top = grid_2, bottom = grid_2)
        ) {
            Text(
                text = billableData.totalBillableTime,
                style = MaterialTheme.typography.h4
            )
        }
        GroupDivider()
    }
}

private val data = BillableData(
    totalBillableTime = "15:00:12",
    billablePercentage = 50.2F
)

@Preview("Billable light theme")
@Composable
fun PreviewBillableLight() {
    ThemedPreview {
        Billable(data)
    }
}

@Preview("Billable dark theme")
@Composable
fun PreviewBillableDark() {
    ThemedPreview(darkTheme = true) {
        Billable(data)
    }
}

