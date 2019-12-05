package com.itomakiweb.android.bank.pages


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.itomakiweb.android.bank.R
import kotlinx.android.synthetic.main.fragment_high_and_low_game.*

/**
 * A simple [Fragment] subclass.
 */
class HighAndLowGameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_high_and_low_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toHighAndLowResult.setOnClickListener {
            val fragment = HighAndLowResultFragment()
            fragmentManager!!.beginTransaction()
                .replace(R.id.highAndLowFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }


}
