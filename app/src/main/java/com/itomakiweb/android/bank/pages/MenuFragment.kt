package com.itomakiweb.android.bank.pages


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itomakiweb.android.bank.R
import kotlinx.android.synthetic.main.fragment_menu.*

/**
 * A simple [Fragment] subclass.
 */
class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toBlackJack.setOnClickListener {
            val intent = Intent(activity, BlackJackActivity::class.java)
            startActivity(intent)
        }

        toHighAndLow.setOnClickListener {
            val intent = Intent(activity, HighAndLowActivity::class.java)
            startActivity(intent)
        }

        toStaffRole.setOnClickListener {
            val intent = Intent(activity, StaffRoleActivity::class.java)
            startActivity(intent)
        }

        toQuestNew.setOnClickListener {
            val intent = Intent(activity, QuestNewActivity::class.java)
            startActivity(intent)
        }
    }
}
