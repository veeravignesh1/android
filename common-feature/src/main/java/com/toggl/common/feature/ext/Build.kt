package com.toggl.common.feature.ext

import com.toggl.common.feature.BuildConfig

object AppBuildConfig {
    val isBuildTypeRelease: Boolean
        get() = BuildConfig.BUILD_TYPE.contentEquals("release")
}
