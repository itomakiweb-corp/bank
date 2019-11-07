package com.itomakiweb.android.bank

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

class HttpPost {
    fun post(url: String, postBody: String) {
        val client: OkHttpClient = OkHttpClient.Builder().build()

        val request = Request.Builder()
            .url(url)
            .post(postBody.)

    }
}