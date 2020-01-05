package com.itomakiweb.android.bank.pages

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.ScopedAppActivity
import kotlinx.android.synthetic.main.activity_high_and_low.*

class HighAndLowActivity : ScopedAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_and_low)

        // TODO 再起動時に問題が起きる可能性有り
        setTopFragment()

        highAndLowFragmentArea.setOnClickListener {
            setMenuFragment()
        }

        backButton.setOnClickListener {

            if (supportFragmentManager.backStackEntryCount > 0) {
                val dialogBuilder = AlertDialog.Builder(this)

                dialogBuilder.setMessage("現在のゲームを終了してメニューに戻りますか ?")

                    .setCancelable(false)
                    .setNegativeButton("いいえ", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })

                    .setPositiveButton("はい", DialogInterface.OnClickListener { dialog, id ->
                        finish()
                    })

                val alert = dialogBuilder.create()

                alert.show()
            } else {
                finish()
            }
        }


        /*
        toHighAndLowRule.setOnClickListener {
            setRuleFragment()
        }
         */
    }

    fun setTopFragment() {
        val fragment = HighAndLowTopFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.highAndLowFragmentArea, fragment)
            .commit()
    }

    fun setMenuFragment() {
        // 設定した後は、イベントに反応させない
        if (supportFragmentManager.backStackEntryCount > 0) return

        val fragment = HighAndLowGameFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.highAndLowFragmentArea, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun setRuleFragment(){
        // 設定した後は、イベントに反応させない
        if (supportFragmentManager.backStackEntryCount > 0) return

        val fragment = HighAndLowRuleFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.highAndLowFragmentArea, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount > 0) {
            val dialogBuilder = AlertDialog.Builder(this)

            dialogBuilder.setMessage("現在のゲームを終了してメニューに戻りますか ?")

                .setCancelable(false)
                .setNegativeButton("いいえ", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

                .setPositiveButton("はい", DialogInterface.OnClickListener { dialog, id ->
                    finish()
                })

            val alert = dialogBuilder.create()

            alert.show()
        } else {
            finish()
        }

    }

    fun setMoney(moneyTotalCurrent: Long, moneyBet: Long? = null) {
        moneyTotalCurrentArea.text = moneyTotalCurrent.toString()
        if (moneyBet != null) {
            moneyBetArea.text = moneyBet.toString()
        }
    }

}
