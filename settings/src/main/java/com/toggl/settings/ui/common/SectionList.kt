package com.toggl.settings.ui.common

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.lazy.LazyColumnItems
import androidx.ui.layout.padding
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import com.toggl.settings.domain.SettingsAction
import com.toggl.settings.domain.SettingsSectionViewModel

@Composable
fun SectionList(
    sectionsList: List<SettingsSectionViewModel>,
    titleMode: SectionTitleMode,
    dispatcher: (SettingsAction) -> Unit,
    navigationBarHeight: Dp
) {
    val lastSection = sectionsList.lastOrNull()
    val firstSection = sectionsList.firstOrNull()
    LazyColumnItems(sectionsList) { section ->

        val bottomPadding = if (section == lastSection) navigationBarHeight else 0.dp
        val withTitle = titleMode == SectionTitleMode.All || (titleMode == SectionTitleMode.AllButFirst && section != firstSection)

        Section(
            section = section,
            dispatcher = dispatcher,
            withTitle = withTitle,
            modifier = Modifier.padding(bottom = bottomPadding)
        )
    }
}

enum class SectionTitleMode {
    All,
    AllButFirst,
    None,
}