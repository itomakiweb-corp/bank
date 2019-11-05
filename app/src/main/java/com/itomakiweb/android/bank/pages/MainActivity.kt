package com.itomakiweb.android.bank.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.GithubApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException


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

        GlobalScope.launch {
            Log.i("Hide", "test")
            try {
                val user = GithubApi.instance.fetchUser("itomakiweb")
                Log.i("Hide", user.toString())
            } catch (e: HttpException) {
                // リクエスト失敗時の処理を行う
            }
        }
    }
}
