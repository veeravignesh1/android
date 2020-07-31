package com.toggl.reports.domain

sealed class ReportsAction {
    object ViewAppeared : ReportsAction()
}

fun ReportsAction.formatForDebug() =
    when (this) {
        ReportsAction.ViewAppeared -> "Reports view appeared"
    }