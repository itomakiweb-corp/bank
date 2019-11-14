package com.itomakiweb.android.bank.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itomakiweb.android.bank.R
import kotlinx.android.synthetic.main.activity_high_and_low.*

class HighAndLowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_and_low)

        // TODO 再起動時に問題が起きる可能性有り
        setTopFragment()

        highAndLowFragment.setOnClickListener {
            setMenuFragment()
        }
    }

    fun setTopFragment() {
        val fragment = HighAndLowTopFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.highAndLowFragment, fragment)
            .commit()
    }

    fun setMenuFragment() {
        // 設定した後は、イベントに反応させない
        if (supportFragmentManager.backStackEntryCount > 0) return

        val fragment = HighAndLowGameFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.highAndLowFragment, fragment)
            .addToBackStack(null)
            .commit()
    }


}
