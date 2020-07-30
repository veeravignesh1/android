package com.toggl.models.domain

enum class WorkspaceFeature(val featureId: Int) {
    Free(0),
    Pro(13),
    @Deprecated("Use specific granular features instead.", level = DeprecationLevel.ERROR)
    Business(15),
    ScheduledReports(50),
    TimeAudits(51),
    LockingTimeEntries(52),
    EditTeamMemberTimeEntries(53),
    EditTeamMemberProfile(54),
    TrackingReminders(55),
    TimeEntryConstraints(56),
    PrioritySupport(57),
    LabourCost(58),
    ReportEmployeeProfitability(59),
    ReportProjectProfitability(60),
    ReportComparative(61),
    ReportDataTrends(62),
    ReportExportXlsx(63),
    Tasks(64),
    ProjectDashboard(65);

    companion object {
        private val values = values()
        fun fromFeatureId(featureId: Int) = values.firstOrNull { it.featureId == featureId }
    }
}
