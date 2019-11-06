package com.itomakiweb.android.bank.libraries

import com.itomakiweb.android.bank.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubUser(
    @Json(name = "node_id")
    val nodeId: String,

    val id: String,

    val url: String,

    val login: String,

    val name: String,

    @Json(name = "avatar_url")
    val avatarUrl: String
)

@JsonClass(generateAdapter = true)
data class GithubRepositoriesOutput(
    @Json(name = "total_count")
    val totalCount: Int
)

@JsonClass(generateAdapter = true)
data class SlackChatMessageInput(
    val text: String,

    val channel: String = BuildConfig.SLACK_CHANNEL
)

@JsonClass(generateAdapter = true)
data class SlackChatMessageOutput(
    val ok: Boolean
)
