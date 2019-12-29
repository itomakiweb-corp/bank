package com.itomakiweb.android.bank.libraries

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.itomakiweb.android.bank.BuildConfig
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

/**
 * @see https://firebase.google.com/docs/firestore/?authuser=0
 * @see https://console.firebase.google.com/u/0/project/itomakiweb-tmp/database/firestore/data~2F000~2F000
 */
class Cloud {
    companion object {
        val instance = Cloud()
        val masterCollectionPath = "master"
        val masterDocumentPath = "android"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var db: FirebaseFirestore

    init {
        // firebase auth
        auth = FirebaseAuth.getInstance()
        // currentUser = auth.currentUser!!

        // firestore
        db = FirebaseFirestore.getInstance()
    }

    fun fetchMaster(onSuccess: (DocumentSnapshot) -> Unit) {
        db.collection(masterCollectionPath)
            .document(masterDocumentPath)
            .get()
            .addOnSuccessListener { master ->
                onSuccess(master)
                Log.d(Ref.TAG_FIRESTORE, "Success fetch master. ${master.id} => ${master.data}")
            }
            .addOnFailureListener { exception ->
                Log.w(Ref.TAG_FIRESTORE, "Failure fetch master.", exception)
            }
    }
}

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
    suspend fun query(
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
