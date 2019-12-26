package com.itomakiweb.android.bank.pages


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.common.reflect.Reflection.getPackageName
import com.google.firebase.auth.FirebaseAuth
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.DeckOfCards
import com.itomakiweb.android.bank.libraries.Rank
import kotlinx.android.synthetic.main.fragment_high_and_low_game.*

/**
 * A simple [Fragment] subclass.
 */
class HighAndLowGameFragment : Fragment() {

    val deck = DeckOfCards()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_high_and_low_game, container, false)
    }

    fun changeCards(){
        val pickCard = deck.draw()
        val resId = resources.getIdentifier(pickCard.drawable,"drawable","com.itomakiweb.android.bank")
        drawCard.setImageResource(resId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        deck.removeCards(Rank.SEVEN)

        deck.shuffle()
    }



}
