package com.toggl.models.validation

sealed class ApiToken {
    class Valid private constructor(val apiToken: String) : ApiToken() {
        override fun equals(other: Any?): Boolean =
            other is Valid && other.apiToken == apiToken

        override fun hashCode(): Int = apiToken.hashCode()

        companion object {
            fun from(apiToken: String) =
                if (apiToken.isBlank() || apiToken.length != 32) Invalid
                else Valid(apiToken)
        }
    }

    object Invalid : ApiToken()

    override fun toString(): String = when (this) {
        is Valid -> apiToken
        Invalid -> ""
    }

    companion object {
        fun from(apiToken: String) =
            Valid.from(apiToken)
    }
}
