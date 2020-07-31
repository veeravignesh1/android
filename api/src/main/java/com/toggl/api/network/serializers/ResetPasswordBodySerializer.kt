package com.toggl.api.network.serializers

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.toggl.api.network.ResetPasswordBody
import java.lang.reflect.Type

class ResetPasswordBodySerializer : JsonSerializer<ResetPasswordBody> {

    override fun serialize(src: ResetPasswordBody, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val feedbackBodyJson = JsonObject()
        feedbackBodyJson.addProperty("email", src.email)
        return feedbackBodyJson
    }
}