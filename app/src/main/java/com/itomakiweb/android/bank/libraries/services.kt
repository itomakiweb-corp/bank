package com.itomakiweb.android.bank.libraries

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.itomakiweb.android.bank.BuildConfig
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

/**
 * https://firebase.google.com/docs/firestore/
 * https://console.firebase.google.com/u/0/project/itomakiweb-tmp/database/firestore/data~2F000~2F000
 */
class Cloud {
    companion object {
        val instance = Cloud()
        val usersCollectionPath = "100-users"
        val mainDocumentPath = "104-bank/001-main"
        val highAndLowCollectionPath = "101-highAndLow"
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
        db.document(mainDocumentPath)
            .get()
            .addOnSuccessListener { master ->
                onSuccess(master)
                Log.d(Ref.TAG_FIRESTORE, "Success fetch master. ${master.id} => ${master.data}")
            }
            .addOnFailureListener { exception ->
                Log.w(Ref.TAG_FIRESTORE, "Failure fetch master.", exception)
            }
    }

    fun createUser(currentUser: FirebaseUser): Task<Void> {
        val user = hashMapOf(
            "uid" to currentUser.uid,
            "nameGoogle" to "",
            "nameFull" to "",
            "nameAlias" to "",
            "iconUrl" to "",
            "role" to "guest",
            "moneyTotalCurrent" to 80000,
            "moneyOwnCurrent" to 0,
            "moneyBorrowCurrent" to 80000,
            "createdAt" to FieldValue.serverTimestamp(),
            "createdBy" to currentUser.uid,
            "updatedAt" to FieldValue.serverTimestamp(),
            "updatedBy" to currentUser.uid,
            "deletedAt" to null,
            "deletedBy" to null
        )

        return db.collection(usersCollectionPath)
            .document(currentUser.uid)
            .set(user)
    }

    fun createHighAndLow(currentUser: FirebaseUser): Task<DocumentReference> {
        val highAndLow = hashMapOf(
            "countSet" to 0,
            "countSetMax" to 1200,
            "countGameTotalSets" to 0,
            "moneyBetRateSets" to 1000,
            "moneyBetTotalSets" to 0,
            "moneyPrizeTotalSets" to 0,
            "moneyResultTotalSets" to 0,
            "moneyResultTotalSetsAverage" to 0,
            "countWinTotalSets" to 0,
            "countWinStreakTotalSets" to 0,
            "countWinStreakMaxTotalSets" to 0,
            "rateWinTotalSets" to 0,
            "countLoseTotalSets" to 0,
            "countLoseStreakTotalSets" to 0,
            "countLoseStreakMaxTotalSets" to 0,
            "rateLoseTotalSets" to 0,
            "createdAt" to FieldValue.serverTimestamp(),
            "createdBy" to currentUser.uid,
            "updatedAt" to FieldValue.serverTimestamp(),
            "updatedBy" to currentUser.uid,
            "deletedAt" to null,
            "deletedBy" to null
        )

        return db.document(mainDocumentPath)
            .collection(highAndLowCollectionPath)
            .add(highAndLow)
    }
}

/**
 * https://developer.github.com/v4/explorer/
 * https://developer.github.com/v4/
 * https://developer.github.com/v3/
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
