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

        setTopFragment()

        // TODO 連続でクリックすると、トップに戻るのに同じクリック数が必要になってしまう
        // 一度押したら、次はイベントに反応させない対応が要りそう
        layout.setOnClickListener {
            setMenuFragment()
        }

        mainTitle.setOnClickListener {
            setMenuFragment()
        }

        mainFragment.setOnClickListener {
            setMenuFragment()
        }
    }

    fun setTopFragment() {
        val fragment = TopFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragment, fragment)
            .commit()
    }

    fun setMenuFragment() {
        val fragment = MenuFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}
