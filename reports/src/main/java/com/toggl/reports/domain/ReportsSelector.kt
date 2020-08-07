package com.toggl.reports.domain

import com.toggl.architecture.core.Selector
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportsSelector @Inject constructor() : Selector<ReportsState, List<ReportsViewModel>> {
    override suspend fun select(state: ReportsState): List<ReportsViewModel> =
        listOf(
            ReportsViewModel.Total(total = "30:00:12"),
            ReportsViewModel.Billable(billableData = BillableData("15:00:12", 50f)),
            ReportsViewModel.Amounts(amounts = listOf(
                AmountsData("2000", "USD"),
                AmountsData("4000", "EUR")
            )),
            ReportsViewModel.DonutChart(segments = listOf(
                DonutChartSegment("Time management", "#06AAF5", .8F, 0F, 288F),
                DonutChartSegment("Important timesheets", "#EA468D", .15F, 288F, 54F),
                DonutChartSegment("Mobile apps", "#F1C33F", .05F, 342F, 18F)
            )),
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
            ReportsViewModel.Error,
            ReportsViewModel.AdvancedReportsOnWeb,
        )
}