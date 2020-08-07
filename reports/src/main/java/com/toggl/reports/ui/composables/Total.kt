package com.toggl.reports.ui.composables

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.material.MaterialTheme
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import com.toggl.common.feature.compose.ThemedPreview

@Composable
fun Total(total: String) {
    Column {
        GroupHeader(text = "Tracked Hours")

        SectionHeader(text = "Total")

        Text(
            text = total,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h3,
            modifier = Modifier.fillMaxWidth()
        )
        GroupDivider()
    }
}


@Preview("Total light theme")
@Composable
fun PreviewTotalLight() {
    ThemedPreview {
        Total("30:00:12")
    }
}

@Preview("Total dark theme")
@Composable
fun PreviewTotalDark() {
    ThemedPreview(darkTheme = true) {
        Total("30:00:12")
    }
}

