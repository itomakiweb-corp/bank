package com.itomakiweb.android.bank.pages


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
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

        //TODO: 仮の値を入れています
        val betMoney = 1000
        //val nextBetMoney = betMoney + 1000

        highAndLowGamePlay(isHigh)

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

    fun highAndLowGamePlay(isHigh: Boolean?) {
        var moneyTotalCurrent = 0L
        var moneyBetRateSets = 0L
        var transactionMoney = 0L

        val gameResult = getGameResult(isHigh)

        val currentUser = auth.currentUser!!

        val db = FirebaseFirestore.getInstance()

        db.collection("highAndLow")
            .whereEqualTo("createdBy", currentUser.uid)
            .get()
            .addOnSuccessListener { highAndLows ->
                val highAndLow = highAndLows.first()
                val highAndLowRef = highAndLow.reference

                moneyBetRateSets = highAndLow["moneyBetRateSets"] as Long
                if(gameResult) {
                    transactionMoney = transactionMoney + moneyBetRateSets * 2
                }
                /*
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

                        val set = sets.first()
                        val setRef = set.reference

                        // set continue
                        setRef.update(
                            mapOf(
                                "countGame" to FieldValue.increment(1)
                            )
                        )
                    }*/
            }
            .addOnFailureListener { exception ->
                Log.w(Ref.TAG_FIRESTORE, "Error getting documents.", exception)
            }

        db.collection("users")
            .whereEqualTo("uid", currentUser.uid)
            .get()
            .addOnSuccessListener { result ->
                for(document in result) {
                    moneyTotalCurrent = document["moneyTotalCurrent"] as Long
                    if(gameResult) {
                        moneyTotalCurrent += transactionMoney
                        document.reference.update(
                            mapOf(
                                "moneyTotalCurrent" to FieldValue.increment(transactionMoney),
                                "moneyOwnCurrent" to FieldValue.increment(transactionMoney)
                            )
                        )
                    }
                    Log.d(Ref.TAG_FIRESTORE, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(Ref.TAG_FIRESTORE, "Error getting documents.", exception)
            }

        setResultText(gameResult)

        messageWindowText.text = getString(R.string.resultMoney, moneyTotalCurrent, moneyBetRateSets)

    }


    fun getGameResult(isHigh: Boolean?): Boolean {
        //TODO: 以下二行の変数はコンストラクタのためだけに実装、ハイアンドロークラスを修正する必要あり
        val deck = DeckOfCards()
        val you = You()
        val highAndLowClass = HighAndLow(deck, you)
        val pickCard = (parentFragment as HighAndLowGameFragment).setDrawCardImage()
        val highOrLow = when(isHigh){
            true -> HighAndLowCall.HIGH
            else -> HighAndLowCall.LOW
        }
        val winOrLose: Boolean = highAndLowClass.getResult(pickCard) == highOrLow

        return winOrLose

    }

    fun setResultText(winOrLose: Boolean) {
        //TODO: 仮の実装、現状お金の動きは無いのでfirestoreを絡めての実装を記述する必要あり
        if(winOrLose) {
            resultText.text = getString(R.string.resultWin)
            resultText.setTextColor(Color.RED)
        } else {
            resultText.text = getString(R.string.resultLose)
            resultText.setTextColor(Color.BLUE)
        }
    }

}
