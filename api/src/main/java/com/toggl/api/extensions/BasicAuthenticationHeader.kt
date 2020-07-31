package com.toggl.api.extensions

import android.util.Base64
import com.toggl.models.validation.ApiToken
import com.toggl.models.validation.Email
import com.toggl.models.validation.Password

private const val apiTokenAuthenticationString = "api_token"

fun Email.Valid.basicAuthenticationWithPassword(password: Password.Valid) =
    basicAuthFromComponents(toString(), password.toString())

fun ApiToken.Valid.basicAuthenticationHeader() =
    basicAuthFromComponents(apiToken, apiTokenAuthenticationString)

private fun basicAuthFromComponents(left: String, right: String): String {
    val authString = "$left:$right"
    val authStringBytes = authString.toByteArray(charset("UTF-8"))
    val encodedString = Base64.encodeToString(authStringBytes, Base64.NO_WRAP)
    return "BASIC $encodedString"
}
