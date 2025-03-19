package com.example.last

import okhttp3.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

object NetworkUtils {
    private val client = OkHttpClient()

    fun post(url: String, params: Map<String, String>, callback: Callback) {
        // Convert parameters to JSON
        val json = Gson().toJson(params)

        // Use toMediaType() for MediaType
        val mediaType = "application/json; charset=utf-8".toMediaType()

        // Use toRequestBody() for RequestBody
        val requestBody = json.toRequestBody(mediaType)

        // Build the request with JSON
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Content-Type", "application/json") // Ensure JSON content type
            .build()

        client.newCall(request).enqueue(callback)
    }
}
