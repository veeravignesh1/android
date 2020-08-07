@file:Suppress("UNUSED_PARAMETER")

package com.toggl.reports.ui.composables

import androidx.compose.Composable
import androidx.ui.layout.ConstraintLayout
import com.toggl.reports.domain.ReportsAction

@Composable
fun AdvancedReportsOnWeb(dispatcher: (ReportsAction) -> Unit) {

    ConstraintLayout {




    }

    GroupHeader(text = "Advanced Reports")

}