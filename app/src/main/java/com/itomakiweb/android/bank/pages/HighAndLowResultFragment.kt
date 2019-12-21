package com.itomakiweb.android.bank.pages


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itomakiweb.android.bank.R
import kotlinx.android.synthetic.main.fragment_high_and_low_result.*

/**
 * A simple [Fragment] subclass.
 */
class HighAndLowResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_high_and_low_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ハイアンドローの実装が済んでいないので仮の値を入れています
        val betMoney = 1000
        val nextBetMoney = betMoney + 1000

        messageWindowText.text = getString(R.string.resultMoney, nextBetMoney)

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


}
