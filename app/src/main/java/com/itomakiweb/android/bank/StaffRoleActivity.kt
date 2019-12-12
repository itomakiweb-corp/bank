package com.itomakiweb.android.bank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_staff_role.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class StaffRoleActivity : AppCompatActivity() {

    val URL = BuildConfig.SLACK_WEBHOOK

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

            val resultText = staffList.shuffled().toString()
            val staffRoleText = "今回の順番は${resultText}です！"

            if(postResultToSlack.isChecked) {
                postResultToSlack.setChecked(false)

                postMessageToSlack(URL, staffRoleText)
            } else {
                staffRoleResult.text = staffRoleText
            }
        }

    }

    fun postMessageToSlack(url: String, message: String) {
        /**
         * 初のhttp通信という事で、とりあえずasyncで試しました。
         * runBlockingを使用する方法にも今後触れて行こうと思います。
         * setTextはUIに関わる処理なので同じrunBlocking内に記述しても良いのでしょうか？
         * coroutineを扱うのは難易度が高いですね、、。
         *
         * @see https://qiita.com/jonghyo/items/bf3e4e06022eebe8e3eb
         */
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val http = HttpPost()
                val httpPost = async(Dispatchers.Default) { http.post(url, message) }.await()

                staffRoleResult.text = "投稿に成功しました！${message}"
            } catch (e: Exception) {
                //この書き方でいいのか怪しいです。
                staffRoleResult.text = e.toString()
            }
        }
    }
}
