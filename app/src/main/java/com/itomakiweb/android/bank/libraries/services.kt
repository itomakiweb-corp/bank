package com.itomakiweb.android.bank.libraries

import com.itomakiweb.android.bank.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("users/{login}")
    suspend fun fetchUser(
        @Path("login") login: String
    ): GithubUser

    @GET("search/repositories")
    suspend fun fetchRepositories(
        @Query(value = "q") query: String, // ex: "test"
        @Query(value = "sort") sort: String, // ex: "stars"
        @Query(value = "order") order: String // ex: "desc"
    ): GithubRepositories
}


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
data class GithubRepositories(
    @Json(name = "total_count")
    val totalCount: Int
)
