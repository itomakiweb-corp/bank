package com.itomakiweb.android.bank.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.GithubApi
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

            staffRoleResult.text = staffList.shuffled().toString()

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
}
