package com.itomakiweb.android.bank.pages


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*

import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.Ref
import kotlinx.android.synthetic.main.fragment_high_and_low_play.*

/**
 * A simple [Fragment] subclass.
 */
class HighAndLowPlayFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_high_and_low_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Auth
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance()

        highButton.setOnClickListener {
            val fragment = HighAndLowResultFragment.newInstance(true)
            fragmentManager!!.beginTransaction()
                .replace(R.id.highAndLowFragmentFrame, fragment)
                .addToBackStack(null)
                .commit()
        }

        lowButton.setOnClickListener {
            val fragment = HighAndLowResultFragment.newInstance(false)
            fragmentManager!!.beginTransaction()
                .replace(R.id.highAndLowFragmentFrame, fragment)
                .addToBackStack(null)
                .commit()
        }

        highAndLowGameCount()
    }

    override fun onStart() {
        super.onStart()
        (parentFragment as HighAndLowGameFragment).unsetDrawCardImage()
    }

    fun highAndLowGameCount() {
        var betMoneyCurrent: Long = 0

        db.collection("highAndLow")
            .whereEqualTo("createdBy", currentUser.uid)
            .get()
            .addOnSuccessListener { highAndLows ->
                val highAndLow = highAndLows.first()
                val highAndLowRef = highAndLow.reference
                val moneyBetRateSets = highAndLow["moneyBetRateSets"] as Long

                //TODO: games関連の修正が必要
                highAndLowRef.update(
                    mapOf(
                        "countGameTotalSets" to FieldValue.increment(1)
                    )
                )
                highAndLowRef.collection("sets")
                    .orderBy("dateTimeSetBegin", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { sets ->
                        // initial access
                        if (sets.size() < 1) {
                            createSet(highAndLowRef, moneyBetRateSets)
                            betMoney(moneyBetRateSets)
                            return@addOnSuccessListener
                        }

                        val set = sets.first()
                        val setRef = set.reference
                        var countGame = set["countGame"] as Long
                        val moneyBetRateGames = set["moneyBetRateGames"] as Long

                        // set start
                        if (countGame >= 10) {
                            setRef.update(
                                mapOf(
                                    "dateTimeSetEnd" to FieldValue.serverTimestamp()
                                )
                            )
                            createSet(highAndLowRef, moneyBetRateSets)
                            betMoney(moneyBetRateSets)
                            return@addOnSuccessListener
                        }

                        // set continue
                        betMoneyCurrent = (countGame + 1) * moneyBetRateGames
                        setRef.update(
                            mapOf(
                                "games" to FieldValue.arrayUnion(
                                    createHashMapGame(betMoneyCurrent)
                                ),
                                "countGame" to FieldValue.increment(1)
                            )
                        )
                        betMoney(betMoneyCurrent)
                    }
            }
            .addOnFailureListener { exception ->
                Log.w(Ref.TAG_FIRESTORE, "Error getting documents.", exception)
            }
    }

    fun createSet(highAndLow: DocumentReference, moneyBetRate: Long) {
        val highAndLowSet = hashMapOf(
            "numberSet" to 1, // TODO
            "countGame" to 1,
            "countGameMax" to 10,
            "moneyBetRateGames" to moneyBetRate,
            "usedCards" to mutableListOf<Any>(),
            "games" to mutableListOf<Any>(
                createHashMapGame()
            ),
            "dateTimeSetBegin" to FieldValue.serverTimestamp(),
            "dateTimeSetEnd" to FieldValue.serverTimestamp(),
            "secondsSet" to 0
        )
        highAndLow.collection("sets")
            .add(highAndLowSet)
            .addOnSuccessListener { set ->
                Log.d(Ref.TAG_FIRESTORE, "highAndLow/sets added with ID: ${set.id}")
            }
    }

    fun createHashMapGame(betMoneyCurrent: Long = 1000): HashMap<String, Any> {
        val game = hashMapOf(
            "numberGame" to 1, // TODO
            "moneyBet" to betMoneyCurrent,
            "moneyPrize" to 0,
            "moneyResult" to 0,
            "call" to "begin",
            "resultGame" to "begin",
            "resultCardSuit" to "begin",
            "resultCardRank" to 0,
            // java.lang.IllegalArgumentException: Invalid data. FieldValue.serverTimestamp() is not currently supported inside arrays
            "dateTimeGameBegin" to System.currentTimeMillis(), // FieldValue.serverTimestamp(),
            "dateTimeGameEnd" to System.currentTimeMillis(), // FieldValue.serverTimestamp(),
            "secondsGame" to 0
        )

        return game
    }

    fun betMoney(betMoneyCurrent: Long) {
        db.collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { user ->
                val moneyTotalCurrent = user["moneyTotalCurrent"] as Long
                user.reference.update(
                    mapOf(
                        "moneyTotalCurrent" to FieldValue.increment(-betMoneyCurrent),
                        "moneyOwnCurrent" to FieldValue.increment(-betMoneyCurrent)
                    )
                )
                Log.d(Ref.TAG_FIRESTORE, "${user.id} => ${user.data}")
                (activity as HighAndLowActivity).setMoney(moneyTotalCurrent, betMoneyCurrent)

            }
            .addOnFailureListener { exception ->
                Log.w(Ref.TAG_FIRESTORE, "Error getting documents.", exception)
            }

    }

}
