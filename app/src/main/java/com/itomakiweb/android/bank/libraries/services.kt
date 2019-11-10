package com.itomakiweb.android.bank.libraries

import com.itomakiweb.android.bank.BuildConfig
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

/**
 * @see https://developer.github.com/v4/explorer/
 * @see https://developer.github.com/v4/
 * @see https://developer.github.com/v3/
 */
interface GithubApi {
    companion object {
        val instance = Retrofit.Builder()
            .baseUrl(BuildConfig.GITHUB_API)
            .addConverterFactory(MoshiConverterFactory.create(
                Moshi.Builder()
                    // .add(KotlinJsonAdapterFactory())
                    .build()
            ))
            .build()
            .create(GithubApi::class.java)
    }

    @POST("graphql")
    @Headers(
        "Authorization: Bearer ${BuildConfig.GITHUB_TOKEN}",
        "Content-Type: application/json; charset=UTF-8"
    )
    suspend fun fetchIssues(
        @Body input: GithubGraphqlInput
    ): GithubGraphqlOutput

    @POST("graphql")
    @Headers(
        "Authorization: Bearer ${BuildConfig.GITHUB_TOKEN}",
        "Content-Type: application/json; charset=UTF-8"
    )
    suspend fun createIssue(
        @Body input: GithubGraphqlInput
    ): GithubCreateIssueOutput

    @GET("users/{login}")
    suspend fun fetchUser(
        @Path("login") login: String
    ): GithubUser

    @GET("search/repositories")
    suspend fun fetchRepositories(
        @Query(value = "q") query: String, // ex: "test"
        @Query(value = "sort") sort: String, // ex: "stars"
        @Query(value = "order") order: String // ex: "desc"
    ): GithubRepositoriesOutput
}

interface SlackApi {
    companion object {
        val instance = Retrofit.Builder()
            .baseUrl(BuildConfig.SLACK_API)
            .addConverterFactory(MoshiConverterFactory.create(
                Moshi.Builder()
                    // .add(KotlinJsonAdapterFactory())
                    .build()
            ))
            .build()
            .create(SlackApi::class.java)
    }

    @POST("chat.postMessage")
    @Headers(
        "Authorization: Bearer ${BuildConfig.SLACK_TOKEN}",
        "Content-Type: application/json; charset=UTF-8"
    )
    suspend fun createChatMessage(
        @Body input: SlackChatMessageInput
    ): SlackChatMessageOutput
}
