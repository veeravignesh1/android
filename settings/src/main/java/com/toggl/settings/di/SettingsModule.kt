package com.toggl.settings.di

import android.content.Context
import com.toggl.common.services.permissions.PermissionCheckerService
import com.toggl.settings.domain.SettingsSelector
import com.toggl.settings.domain.SettingsStructureBlueprint
import com.toggl.settings.domain.SingleChoiceSettingSelector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object FragmentSettingsModule {
    @Provides
    @FragmentScoped
    @ProvideMainSettingsSelector
    internal fun mainSettingsSelector(@ActivityContext context: Context, permissionCheckerService: PermissionCheckerService) =
        SettingsSelector(context, permissionCheckerService) { SettingsStructureBlueprint.mainSections }

    @Provides
    @FragmentScoped
    @ProvideAboutSettingsSelector
    internal fun aboutSettingsSelector(@ActivityContext context: Context, permissionCheckerService: PermissionCheckerService) =
        SettingsSelector(context, permissionCheckerService) { listOf(SettingsStructureBlueprint.aboutSection) }

    @Provides
    @FragmentScoped
    @ProvideCalendarSettingsSelector
    internal fun calendarSettingsSelector(
        @ActivityContext context: Context,
        permissionCheckerService: PermissionCheckerService,
        settingsStructureBlueprint: SettingsStructureBlueprint
    ) = SettingsSelector(context, permissionCheckerService, settingsStructureBlueprint::calendarSections)

    @Provides
    @FragmentScoped
    internal fun singleChoiceSettingsSelector(@ActivityContext context: Context) = SingleChoiceSettingSelector(context)
}
