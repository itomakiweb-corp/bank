package com.itomakiweb.android.bank.libraries

import com.itomakiweb.android.bank.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubGraphqlInput(
    val query: String = """mutation(${'$'}input: CreateIssueInput!) {
    createIssue(input: ${'$'}input) {
        issue {
            id
            url
            title
        }
    }
}
""",

    val variables: Any
)

@JsonClass(generateAdapter = true)
data class GithubCreateIssueInput(
    val title: String,

    val body: String,

    val assigneeIds: List<String>,

    val labelIds: List<String>,

    val projectIds: List<String> = BuildConfig.GITHUB_TODO_PROJECT_IDS.toList(),

    val milestoneId: String = BuildConfig.GITHUB_MILESTONE_ID,

    val repositoryId: String = BuildConfig.GITHUB_REPOSITORY_ID
)

@JsonClass(generateAdapter = true)
data class GithubUpdateIssueInput(
    val id: String,

    val state: String,

    val title: String,

    val body: String,

    val assigneeIds: List<String>,

    val labelIds: List<String>,

    val projectIds: List<String> = BuildConfig.GITHUB_TODO_PROJECT_IDS.toList(),

    val milestoneId: String = BuildConfig.GITHUB_MILESTONE_ID,

    val repositoryId: String = BuildConfig.GITHUB_REPOSITORY_ID
)

@JsonClass(generateAdapter = true)
data class GithubCreateIssueOutput(
    val createIssue: GithubIssueParent
)

@JsonClass(generateAdapter = true)
data class GithubIssueParent(
    val issue: GithubIssue
)

@JsonClass(generateAdapter = true)
data class GithubIssue(
    val id: String,

    val url: String,

    val title: String
)

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
