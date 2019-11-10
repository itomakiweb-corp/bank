package com.itomakiweb.android.bank.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itomakiweb.android.bank.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = TopFragment()
        val fragmentManager = this.supportFragmentManager
        fragmentManager.beginTransaction()
            .add(R.id.fragmentFrame, fragment)
            .commit()

        // TODO 連続でクリックすると、トップに戻るのに同じクリック数が必要になってしまう
        // 一度押したら、次はイベントに反応させない対応が要りそう
        titleText.setOnClickListener {
            setMenuFragment()
        }

        fragmentFrame.setOnClickListener {
            setMenuFragment()
        }
    }

    fun setMenuFragment() {
        val fragment = MenuFragment()
        val fragmentManager = this.supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentFrame, fragment)
            .addToBackStack(null)
            .commit()
    }
}
