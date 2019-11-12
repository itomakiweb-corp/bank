package com.itomakiweb.android.bank

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

class HttpPost {
    fun post(url: String, body: String): String? {
        val client: OkHttpClient = OkHttpClient.Builder().build()

        val json = JSONObject()
        json.put("text", body)

        val mediaTypeJson = MediaType.get("application/json; charset=utf-8")

        val postBody = RequestBody.create(mediaTypeJson, json.toString())

        val request = Request.Builder()
            .url(url)
            .post(postBody)
            .build()
        val response = client.newCall(request).execute()

        val result: String? = response.body()?.string()
        return result

    }
}