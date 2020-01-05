package com.itomakiweb.android.bank.pages


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.*
import kotlinx.android.synthetic.main.fragment_high_and_low_result.*

/**
 * A simple [Fragment] subclass.
 */

val HIGH_OR_LOW = "HIGH_OR_LOW"

class HighAndLowResultFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var db: FirebaseFirestore
    private lateinit var pickCard: Card

    companion object{
        fun newInstance(isHigh: Boolean): HighAndLowResultFragment {
            val bundle = Bundle()
            bundle.putBoolean(HIGH_OR_LOW, isHigh)
            val fragment = HighAndLowResultFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var isHigh: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isHigh = it.getBoolean(HIGH_OR_LOW)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_high_and_low_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!
        currentUser = auth.currentUser!!

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance()

        nextButton.setOnClickListener {
            val fragment = HighAndLowPlayFragment()
            fragmentManager!!.beginTransaction()
                .replace(R.id.highAndLowFragmentFrame, fragment)
                .addToBackStack(null)
                .commit()
        }

        backButton.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        (activity as ScopedAppActivity).showProgressBar()

        highAndLowGamePlay(isHigh!!)
    }

    fun highAndLowGamePlay(isHigh: Boolean) {
        var moneyTotalCurrent = 0L
        var moneyBetRateSets = 0L
        var transactionMoney = 0L

        val gameResult = getGameResult(isHigh)

        db.collection("highAndLow")
            .whereEqualTo("createdBy", currentUser.uid)
            .get()
            .addOnSuccessListener { highAndLows ->
                val highAndLow = highAndLows.first()
                val highAndLowRef = highAndLow.reference

                highAndLowRef.collection("sets")
                    .orderBy("dateTimeSetBegin", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { sets ->
                        val set = sets.first()
                        val setRef = set.reference
                        var countGame = set["countGame"] as Long
                        val moneyBetRateGames = set["moneyBetRateGames"] as Long
                        val games = set["games"] as MutableList<Any>
                        val game = games.last() as HashMap<String, Any>
                        val moneyBet = game["moneyBet"] as Long
                        val moneyPrize = if (gameResult) moneyBet * 2 else 0
                        game["moneyPrize"] = moneyPrize
                        game["moneyResult"] = moneyPrize - moneyBet
                        game["call"] = if (isHigh) HighAndLowCall.HIGH else HighAndLowCall.LOW
                        game["resultGame"] = if (gameResult) ResultGame.WIN else ResultGame.LOSE
                        Log.d("debuggg", pickCard.toString())
                        game["resultCardSuit"] = pickCard.suit
                        game["resultCardRank"] = pickCard.rank
                        game["dateTimeGameEnd"] = System.currentTimeMillis()
                        // game["secondsGame"] = game["dateTimeGameStart"] as Long - game["dateTimeGameEnd"] as Long
                        games[games.lastIndex] = game
                        setRef.update(
                            mapOf(
                                "games" to games
                            )
                        )
                        val moneyBetRateNext = Ref.getBet(countGame, moneyBetRateGames)

                        setMoney(moneyPrize, gameResult, moneyBetRateNext, moneyPrize)
                    }
            }
            .addOnFailureListener { exception ->
                Log.w(Ref.TAG_FIRESTORE, "Error getting documents.", exception)
                (activity as ScopedAppActivity).hideProgressBar()
            }
    }

    fun setMoney(transactionMoney: Long, gameResult: Boolean, moneyBetRateNext: Long, moneyPrize: Long) {
        db.collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { user ->
                val moneyTotalCurrent = user["moneyTotalCurrent"] as Long + transactionMoney
                if (gameResult) {
                    user.reference.update(
                        mapOf(
                            "moneyTotalCurrent" to FieldValue.increment(transactionMoney),
                            "moneyOwnCurrent" to FieldValue.increment(transactionMoney)
                        )
                    )
                }
                Log.d(Ref.TAG_FIRESTORE, "${user.id} => ${user.data}")

                setResultText(gameResult, moneyPrize)
                (activity as HighAndLowActivity).setMoney(moneyTotalCurrent)

                messageWindowText.text = getString(R.string.resultMoney, moneyTotalCurrent, moneyBetRateNext)

                (activity as ScopedAppActivity).hideProgressBar()
            }
            .addOnFailureListener { exception ->
                Log.w(Ref.TAG_FIRESTORE, "Error getting documents.", exception)
                (activity as ScopedAppActivity).hideProgressBar()
            }
    }


    fun getGameResult(isHigh: Boolean?): Boolean {
        //TODO: 以下二行の変数はコンストラクタのためだけに実装、ハイアンドロークラスを修正する必要あり
        val deck = DeckOfCards()
        val you = You()
        val highAndLowClass = HighAndLow(deck, you)
        pickCard = (parentFragment as HighAndLowGameFragment).setDrawCardImage()
        val highOrLow = when(isHigh){
            true -> HighAndLowCall.HIGH
            else -> HighAndLowCall.LOW
        }
        val winOrLose: Boolean = highAndLowClass.getResult(pickCard) == highOrLow

        return winOrLose

    }

    fun setResultText(winOrLose: Boolean, moneyPrize: Long) {
        //TODO: 仮の実装、現状お金の動きは無いのでfirestoreを絡めての実装を記述する必要あり
        if(winOrLose) {
            resultText.text = getString(R.string.resultWin, moneyPrize)
            resultText.setTextColor(Color.RED)
        } else {
            resultText.text = getString(R.string.resultLose)
            resultText.setTextColor(Color.BLUE)
        }
    }

}
