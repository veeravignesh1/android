package com.toggl.reports.ui.composables

import androidx.compose.Composable
import androidx.compose.collectAsState
import androidx.compose.getValue
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.lazy.LazyColumnItems
import androidx.ui.layout.padding
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import com.toggl.common.feature.compose.ThemedPreview
import com.toggl.common.feature.compose.theme.TogglTheme
import com.toggl.common.feature.compose.theme.grid_2
import com.toggl.reports.R
import com.toggl.reports.domain.AmountsData
import com.toggl.reports.domain.BillableData
import com.toggl.reports.domain.DonutChartLegendInfo
import com.toggl.reports.domain.ReportsAction
import com.toggl.reports.domain.ReportsViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun ReportsPage(
    viewModels: Flow<List<ReportsViewModel>>,
    statusBarHeight: Dp,
    dispatcher: (ReportsAction) -> Unit
) {
    val items by viewModels.collectAsState(emptyList())
    TogglTheme {
        ReportsContent(items, statusBarHeight, dispatcher)
    }
}

@Composable
fun ReportsContent(
    items: List<ReportsViewModel>,
    statusBarHeight: Dp,
    dispatcher: (ReportsAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = statusBarHeight),
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                title = { Text(text = stringResource(R.string.reports)) }
            )
        },
        bodyContent = {
            LazyColumnItems(
                items = items,
                modifier = Modifier.padding(start = grid_2, end = grid_2)
            ) { viewModel ->
                when (viewModel) {
                    is ReportsViewModel.Total -> Total(viewModel.total)
                    is ReportsViewModel.Billable -> Billable(viewModel.billableData)
                    is ReportsViewModel.Amounts -> Amounts(viewModel.amounts)
                    is ReportsViewModel.BarChart -> BarChart(viewModel, dispatcher)
                    is ReportsViewModel.DonutChart -> DonutChart(viewModel.segments)
                    is ReportsViewModel.DonutChartLegend -> DonutChartLegend(viewModel.legend)
                    is ReportsViewModel.Error -> Error(viewModel.errorMessage)
                    is ReportsViewModel.AdvancedReportsOnWeb -> AdvancedReportsOnWeb(dispatcher)
                }
            }
        }
    )
}

private val data = listOf(
    ReportsViewModel.Total(total = "30:00:12"),
    ReportsViewModel.Billable(billableData = BillableData("15:00:12", 50f)),
    ReportsViewModel.Amounts(amounts = listOf(
        AmountsData("2000", "USD"),
        AmountsData("4000", "EUR")
    )),
    ReportsViewModel.DonutChart(segments = emptyList()),
    ReportsViewModel.DonutChartLegend(
        legend = DonutChartLegendInfo(
            "Time management",
            "#06AAF5",
            "80%",
            "16:12:00"
        )
    ),
    ReportsViewModel.DonutChartLegend(
        legend = DonutChartLegendInfo(
            "Important timesheets",
            "#EA468D",
            "15%",
            "9:00:00"
        )
    ),
    ReportsViewModel.DonutChartLegend(
        legend = DonutChartLegendInfo(
            "Mobile apps",
            "#F1C33F",
            "5%",
            "7:12:00"
        )
    ),
    ReportsViewModel.Error("Error message"),
    ReportsViewModel.AdvancedReportsOnWeb,
)

@Preview("Reports page light theme")
@Composable
fun PreviewReportsPageLight() {
    ThemedPreview {
        ReportsContent(data, 10.dp) { }
    }
}

@Preview("Reports page dark theme")
@Composable
fun PreviewReportsPageDark() {
    ThemedPreview(darkTheme = true) {
        ReportsContent(data, 10.dp) { }
    }
}

