package com.itomakiweb.android.bank.pages

import android.os.Bundle
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.ScopedAppActivity
import kotlinx.android.synthetic.main.activity_black_jack.*

class BlackJackActivity : ScopedAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_jack)

        toBlackJackRule.setOnClickListener {
            setRuleFragment()
        }
    }
    fun setRuleFragment() {
        // 設定した後は、イベントに反応させない
        if (supportFragmentManager.backStackEntryCount > 0) return

        val fragment = BlackJackRuleFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.blackJackFragmentArea, fragment)
            .addToBackStack(null)
            .commit()
    }
}
