package com.itomakiweb.android.bank.pages

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.itomakiweb.android.bank.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 画面が再利用されていない場合のみ、生成
        if (savedInstanceState == null) {
            setTopFragment()
        }

        layout.setOnClickListener {
            setMenuFragment()
        }

        mainTitle.setOnClickListener {
            setMenuFragment()
        }

        mainFragment.setOnClickListener {
            setMenuFragment()
        }

        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name
        val user = hashMapOf(
            "message" to "test messsage",
            "username" to "ikki",
            "age" to 34
        )

        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("test", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("test", "Error adding document", e)
            }

    }

    fun setTopFragment() {
        val fragment = MainTopFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragment, fragment)
            .commit()
    }

    fun setMenuFragment() {
        // 設定した後は、イベントに反応させない
        if (supportFragmentManager.backStackEntryCount > 0) return

        val fragment = MainMenuFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}
