package com.toggl.api.extensions

import com.toggl.api.BuildConfig

object AppBuildConfig {
    val isBuildTypeRelease: Boolean
        get() = BuildConfig.BUILD_TYPE.contentEquals("release")
}
