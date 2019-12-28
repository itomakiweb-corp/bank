package com.itomakiweb.android.bank.pages


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.DeckOfCards
import com.itomakiweb.android.bank.libraries.HighAndLow
import com.itomakiweb.android.bank.libraries.HighAndLowCall
import com.itomakiweb.android.bank.libraries.You
import kotlinx.android.synthetic.main.fragment_high_and_low_game.*
import kotlinx.android.synthetic.main.fragment_high_and_low_result.*

/**
 * A simple [Fragment] subclass.
 */

val HIGH_OR_LOW = "HIGH_OR_LOW"

class HighAndLowResultFragment : Fragment() {

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


        //TODO: 仮の値を入れています
        val betMoney = 1000
        val nextBetMoney = betMoney + 1000

        messageWindowText.text = getString(R.string.resultMoney, nextBetMoney)

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
        //TODO: 以下二行の変数はコンストラクタのためだけに実装、ハイアンドロークラスを修正する必要あり
        val deck = DeckOfCards()
        val you = You()
        val highAndLowClass = HighAndLow(deck, you)
        val pickCard = (parentFragment as HighAndLowGameFragment).changeCards()
        val highOrLow = when(isHigh){
            true -> HighAndLowCall.HIGH
            else -> HighAndLowCall.LOW
        }
        val winOrLose: Boolean = highAndLowClass.getResult(pickCard) == highOrLow

        moneyTransaction(winOrLose)

    }

    fun moneyTransaction(winOrLose: Boolean) {
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
