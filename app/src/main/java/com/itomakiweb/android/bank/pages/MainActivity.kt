package com.itomakiweb.android.bank.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.itomakiweb.android.bank.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFragment(TopFragment())

        // TODO 連続でクリックすると、トップに戻るのに同じクリック数が必要になってしまう
        // 一度押したら、次はイベントに反応させない対応が要りそう
        layout.setOnClickListener {
            setFragment(MenuFragment())
        }

        mainTitle.setOnClickListener {
            setFragment(MenuFragment())
        }

        mainFragment.setOnClickListener {
            setFragment(MenuFragment())
        }
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}
