package com.itomakiweb.android.bank.pages


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.Card
import com.itomakiweb.android.bank.libraries.DeckOfCards
import com.itomakiweb.android.bank.libraries.Rank
import com.itomakiweb.android.bank.libraries.Ref
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

    fun setDrawCardImage(): Card {
        Log.d(Ref.TAG_DEBUG, deck.toString())
        val pickCard = deck.draw()
        drawCard.setImageResource(pickCard.getResourceId(context!!))
        Log.d(Ref.TAG_DEBUG, pickCard.toString())

        return pickCard
    }

    fun unsetDrawCardImage() {
        drawCard.setImageResource(R.drawable.card_blank_00)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        deck.removeCards(Rank.SEVEN)

        deck.shuffle()
    }



}
