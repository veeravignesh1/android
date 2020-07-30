package com.toggl.api.network.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.toggl.models.domain.User
import com.toggl.models.validation.ApiToken
import com.toggl.models.validation.Email
import java.lang.IllegalStateException
import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<User> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): User {

        val jsonObject = json?.asJsonObject ?: throw IllegalStateException()
        val apiToken = jsonObject["api_token"].asString.let { ApiToken.from(it) } as? ApiToken.Valid ?: throw IllegalStateException()
        val email = jsonObject["email"].asString.let { Email.from(it) } as? Email.Valid ?: throw IllegalStateException()

        return User(
            id = jsonObject["id"].asLong,
            apiToken = apiToken,
            email = email,
            name = jsonObject["fullname"].asString,
            defaultWorkspaceId = jsonObject["default_workspace_id"].asLong
        )
    }
}