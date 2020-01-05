package com.itomakiweb.android.bank.pages


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itomakiweb.android.bank.R
import kotlinx.android.synthetic.main.fragment_high_and_low_top.*

/**
 * A simple [Fragment] subclass.
 */
class HighAndLowTopFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_high_and_low_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toHighAndLowGame.setOnClickListener {
            val fragment = HighAndLowGameFragment()
            fragmentManager!!.beginTransaction()
                .replace(R.id.highAndLowFragmentArea, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

}
