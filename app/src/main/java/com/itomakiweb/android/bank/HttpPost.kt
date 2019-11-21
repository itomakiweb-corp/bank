package com.itomakiweb.android.bank

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class HttpPost {
    fun post(url: String, body: String) {
        val client: OkHttpClient = OkHttpClient.Builder().build()

        val json = JSONObject()
        json.put("text", body)

        val postBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .post(postBody)
            .build()

        val response = client.newCall(request).execute()
    }
}