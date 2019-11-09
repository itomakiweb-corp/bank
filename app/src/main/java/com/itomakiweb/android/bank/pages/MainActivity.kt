package com.itomakiweb.android.bank.pages

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.itomakiweb.android.bank.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toBlackJack.setOnClickListener {
            val intent = Intent(this, BlackJackActivity::class.java)
            startActivity(intent)
        }

        toHighAndLow.setOnClickListener {
            val intent = Intent(this, HighAndLowActivity::class.java)
            startActivity(intent)
        }

        toStaffRole.setOnClickListener {
            val intent = Intent(this, StaffRoleActivity::class.java)
            startActivity(intent)
        }

        toQuestNew.setOnClickListener {
            val intent = Intent(this, QuestNewActivity::class.java)
            startActivity(intent)
        }

        layout.setOnClickListener {
            testVisible.visibility = View.GONE
            toBlackJack.visibility = View.VISIBLE
        }



    }
}
