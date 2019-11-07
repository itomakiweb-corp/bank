package com.itomakiweb.android.bank.pages

import android.os.Bundle
import android.util.Log
import com.itomakiweb.android.bank.BuildConfig
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.ScopedAppActivity
import com.itomakiweb.android.bank.libraries.SlackApi
import com.itomakiweb.android.bank.libraries.SlackChatMessageInput
import kotlinx.android.synthetic.main.activity_staff_role.*
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class StaffRoleActivity : ScopedAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_role)

        showStaffRoleResult.setOnClickListener {
            val staffList: MutableList<String> = mutableListOf()
            if (nameIkki.isChecked){
                staffList.add("ikki")
            }
            if (nameKazucharo.isChecked){
                staffList.add("kazucharo")
            }
            if (nameYu.isChecked){
                staffList.add("yu-")
            }
            if (nameAda.isChecked){
                staffList.add("Ada")
            }
            if (nameBe.isChecked){
                staffList.add("be-")
            }
            if (nameHide.isChecked){
                staffList.add("Hide")
            }
            if (nameNobu.isChecked){
                staffList.add("Nobu")
            }

            val resultText = getString(
                R.string.staffRoleResult,
                staffList.shuffled().toString()
            )
            staffRoleResult.text = resultText

            // local.propertiesに何も設定していない場合、文字列でnullとなる
            if (BuildConfig.SLACK_TOKEN != "null") {
                createSlackChatMessage(resultText)
            }
        }
    }

    fun createSlackChatMessage(text: String) {
        // TODO いずれ、処理の共通化を検討
        runBlocking {
            try {
                val input = SlackChatMessageInput(
                    text = text
                )
                Log.i("api", "${input}")

                val message = SlackApi.instance.createChatMessage(input)

                Log.i("api", "${message}")
            } catch (e: Exception) {
                val resultText = e.message
                staffRoleResult.text = resultText

                Log.w("api", resultText, e)
            }
        }
    }
}
