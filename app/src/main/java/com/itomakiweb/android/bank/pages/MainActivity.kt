package com.itomakiweb.android.bank.pages

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.itomakiweb.android.bank.BuildConfig
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.Cloud
import com.itomakiweb.android.bank.libraries.Ref
import com.itomakiweb.android.bank.libraries.ScopedAppActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : ScopedAppActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Firebase Auth
        auth = FirebaseAuth.getInstance()

        // force update
        needUpdateIf(savedInstanceState)
    }

    /**
     * if app version code is lower than required version code managed by cloud,
     * then show need update and disable event
     */
    fun needUpdateIf(savedInstanceState: Bundle?) {
        Cloud.instance.fetchMaster {
            val requiredVersionCode = it["requiredVersionCodeAndroid"] as Long
            if (BuildConfig.VERSION_CODE < requiredVersionCode) {
                buttonAnonymousSignOut.setText(R.string.needUpdate)
                buttonAnonymousSignOut.visibility = View.VISIBLE
                buttonAnonymousSignOut.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Ref.URL_STORE))
                    startActivity(intent)
                }

                return@fetchMaster
            }

            /*
            buttonAnonymousSignIn.setOnClickListener {
                signInAnonymously()
            }

             */
            buttonAnonymousSignOut.setOnClickListener {
                //signOut()
            }

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

            mainFragmentArea.setOnClickListener {
                setMenuFragment()
            }
        }
    }

    fun setTopFragment() {
        val fragment = MainTopFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentArea, fragment)
            .commit()
    }

    fun setMenuFragment() {
        // 設定した後は、イベントに反応させない
        if (supportFragmentManager.backStackEntryCount > 0) return

        val fragment = MainMenuFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentArea, fragment)
            .addToBackStack(null)
            .commit()
    }

    public override fun onStart() {
        super.onStart()

        showProgressBar()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.d(Ref.TAG_AUTH, "signInAnonymously:begin")
            signInAnonymously()
        } else {
            hideProgressBar()
        }
    }

    private fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(Ref.TAG_AUTH, "signInAnonymously:success")
                    val currentUser = auth.currentUser!!
                    Cloud.instance.createUser(currentUser)
                        .addOnSuccessListener {
                            Log.d(
                                Ref.TAG_FIRESTORE,
                                "DocumentSnapshot added with ID: ${currentUser.uid}"
                            )
                            hideProgressBar()
                        }
                        .addOnFailureListener { e ->
                            Log.w(Ref.TAG_FIRESTORE, "Error adding document", e)
                            hideProgressBar()
                        }
                    Cloud.instance.createHighAndLow(currentUser)
                        .addOnSuccessListener { documentReference ->
                            Log.d(
                                Ref.TAG_FIRESTORE,
                                "highAndLow added with ID: ${documentReference.id}"
                            )
                            hideProgressBar()
                        }
                        .addOnFailureListener { e ->
                            Log.w(Ref.TAG_FIRESTORE, "Error adding document", e)
                            hideProgressBar()
                        }
                } else {
                    Log.w(Ref.TAG_AUTH, "signInAnonymously:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgressBar()
                }
            }
    }

    private fun signOut() {
        auth.signOut()
    }

    override fun onBackPressed() {
        val dialogBuilder =  AlertDialog.Builder(this)

        dialogBuilder.setMessage("このアプリを終了しますか ?")

            .setCancelable(false)
            .setNegativeButton("いいえ", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

            .setPositiveButton("はい", DialogInterface.OnClickListener {
                    dialog, id -> finish()
            })

        val alert = dialogBuilder.create()

        alert.show()
    }
}
