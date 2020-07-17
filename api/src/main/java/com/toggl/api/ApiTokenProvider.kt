package com.toggl.api

import com.toggl.models.validation.ApiToken

interface ApiTokenProvider {
    fun getApiToken(): ApiToken
}