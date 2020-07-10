package com.toggl.environment.ext

import com.toggl.environment.BuildConfig

object AppBuildConfig {
    val isBuildTypeRelease: Boolean
        get() = BuildConfig.BUILD_TYPE.contentEquals("release")
}
