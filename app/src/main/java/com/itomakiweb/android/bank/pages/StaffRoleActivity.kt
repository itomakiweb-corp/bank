package com.itomakiweb.android.bank.pages

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.SlackApi
import com.itomakiweb.android.bank.libraries.SlackPostMessageInput
import kotlinx.android.synthetic.main.activity_staff_role.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StaffRoleActivity : AppCompatActivity() {

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

            val result = staffList.shuffled().toString()
            staffRoleResult.text = result

// TODO no use GlobalScope, tokenセット時のみ実行
            GlobalScope.launch {
                Log.i("Hide", "test")
                try {
                    val input = SlackPostMessageInput(
                        text = result
                    )
                    val message = SlackApi.instance.createMessage(input)
                    Log.i("Hide", message.toString())
                } catch (e: HttpException) {
                    // リクエスト失敗時の処理を行う
                }
            }
        }

    }
}
