package com.toggl.common.feature.navigation

import android.net.Uri

internal sealed class BackStackOperation {
    data class Push(val deepLink: Uri) : BackStackOperation()
    object Pop : BackStackOperation()
}
