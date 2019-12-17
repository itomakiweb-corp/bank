package com.itomakiweb.android.bank.pages


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.itomakiweb.android.bank.R

/**
 * A simple [Fragment] subclass.
 */
class BlackJackRuleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_black_jack_rule, container, false)
    }


}
