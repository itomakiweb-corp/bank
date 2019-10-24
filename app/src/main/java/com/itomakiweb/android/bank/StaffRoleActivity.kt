package com.itomakiweb.android.bank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_staff_role.*

class StaffRoleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_role)

        val staffList: MutableList<String> = mutableListOf()

        showStaffRoleResult.setOnClickListener {
            if (nameIkki.isChecked){
                staffList.add("ikki")
            }

            staffRoleResult.text = staffList.shuffled().toString()
        }

    }
}
