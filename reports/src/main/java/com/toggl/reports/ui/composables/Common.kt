package com.toggl.reports.ui.composables

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.padding
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.text.font.FontWeight
import com.toggl.common.feature.compose.theme.grid_2
import com.toggl.common.feature.compose.theme.grid_3

@Composable
internal fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body2,
        fontWeight = FontWeight.Bold
    )
}

@Composable
internal fun GroupHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(bottom = grid_2)
    )
}

@Composable
internal fun GroupDivider() {
    Divider(
        modifier = Modifier.padding(
            top = grid_3,
            bottom = grid_2
        )
    )
}