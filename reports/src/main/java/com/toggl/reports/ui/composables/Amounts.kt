package com.toggl.reports.ui.composables

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.padding
import androidx.ui.material.MaterialTheme
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.dp
import com.toggl.common.feature.compose.theme.grid_3
import com.toggl.reports.domain.AmountsData

@Composable
fun Amounts(amounts: List<AmountsData>) {
    Column {

        SectionHeader(text = "Amount")

        for (amount in amounts) {
            val padding = if (amounts.indexOf(amount) == 0) 0.dp else grid_3
            Row(
                verticalGravity = Alignment.Bottom,
                modifier = Modifier.padding(top = padding)
            ) {
                Text(
                    text = amount.amount,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = grid_3)
                )

                Text(
                    text = amount.currency,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        GroupDivider()
    }
}