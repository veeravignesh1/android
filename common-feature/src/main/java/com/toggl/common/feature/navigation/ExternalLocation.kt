package com.toggl.common.feature.navigation

import android.content.Context
import android.net.Uri
import com.toggl.common.feature.R

enum class ExternalLocation {
    Help,
    PrivacyPolicy,
    TermsOfService
}

fun ExternalLocation.getExternalUri(context: Context): Uri {
    val linkResId = when (this) {
        ExternalLocation.Help -> R.string.help_link
        ExternalLocation.PrivacyPolicy -> R.string.privacy_policy_link
        ExternalLocation.TermsOfService -> R.string.terms_of_service_link
    }
    return Uri.parse(context.getString(linkResId))
}