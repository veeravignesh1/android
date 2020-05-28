package com.toggl.models.domain

data class Project(
    val id: Long,
    val name: String,
    val color: String,
    val active: Boolean,
    val isPrivate: Boolean,
    val billable: Boolean?,
    val workspaceId: Long,
    val clientId: Long?
) {
    companion object {
        val defaultColors = listOf(
            "#0B83D9", "#9E5BD9", "#D94182", "#E36A00", "#BF7000",
            "#C7AF14", "#D92B2B", "#2DA608", "#06A893", "#C9806B",
            "#465BB3", "#990099", "#566614", "#525266"
        )
    }
}
