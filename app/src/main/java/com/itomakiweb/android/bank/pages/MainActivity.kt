package com.itomakiweb.android.bank.pages

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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
     * if app version code is lower than required version code,
     * then show need update and disable event
     */
    fun needUpdateIf(savedInstanceState: Bundle?) {
        Cloud.instance.fetchMaster {
            val requiredVersionCode = it["requiredVersionCode"] as Long
            if (BuildConfig.VERSION_CODE < requiredVersionCode) {
                buttonAnonymousSignOut.setText(R.string.needUpdate)
                buttonAnonymousSignOut.visibility = View.VISIBLE

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

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()

        showProgressBar()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.d(Ref.TAG_AUTH, "signInAnonymously:begin")
            signInAnonymously()
        } else {
            hideProgressBar()
        }
        // updateUI(currentUser)
    }
    // [END on_start_check_user]

    private fun signInAnonymously() {
        // [START signin_anonymously]
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Ref.TAG_AUTH, "signInAnonymously:success")
                    val currentUser = auth.currentUser!!
                    // Log.d(TAG, user.toString())
                    createUser(currentUser)
                    // updateUI(user)
                    createHighAndLow(currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Ref.TAG_AUTH, "signInAnonymously:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    // updateUI(null)
                    hideProgressBar()
                }

                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        // [END signin_anonymously]
    }

    private fun createUser(currentUser: FirebaseUser) {
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name
        val user = hashMapOf(
            "uid" to currentUser.uid,
            "nameGoogle" to "",
            "nameFull" to "",
            "nameAlias" to "",
            "iconUrl" to "",
            "role" to "guest",
            "moneyTotalCurrent" to 80000,
            "moneyOwnCurrent" to 0,
            "moneyBorrowCurrent" to 80000,
            "createdAt" to FieldValue.serverTimestamp(),
            "createdBy" to currentUser.uid,
            "updatedAt" to FieldValue.serverTimestamp(),
            "updatedBy" to currentUser.uid,
            "deletedAt" to null,
            "deletedBy" to null
        )

        // Add a new document with a generated ID
        db.collection("users")
            .document(currentUser.uid)
            .set(user)
            .addOnSuccessListener {
                Log.d(Ref.TAG_FIRESTORE, "DocumentSnapshot added with ID: ${currentUser.uid}")
                hideProgressBar()
            }
            .addOnFailureListener { e ->
                Log.w(Ref.TAG_FIRESTORE, "Error adding document", e)
                hideProgressBar()
            }

    }

    private fun createHighAndLow(currentUser: FirebaseUser) {
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name
        val highAndLow = hashMapOf(
            "countSet" to 0,
            "countSetMax" to 1200,
            "countGameTotalSets" to 0,
            "moneyBetRateSets" to 1000,
            "moneyBetTotalSets" to 0,
            "moneyPrizeTotalSets" to 0,
            "moneyResultTotalSets" to 0,
            "moneyResultTotalSetsAverage" to 0,
            "countWinTotalSets" to 0,
            "countWinStreakTotalSets" to 0,
            "countWinStreakMaxTotalSets" to 0,
            "rateWinTotalSets" to 0,
            "countLoseTotalSets" to 0,
            "countLoseStreakTotalSets" to 0,
            "countLoseStreakMaxTotalSets" to 0,
            "rateLoseTotalSets" to 0,
            "createdAt" to FieldValue.serverTimestamp(),
            "createdBy" to currentUser.uid,
            "updatedAt" to FieldValue.serverTimestamp(),
            "updatedBy" to currentUser.uid,
            "deletedAt" to null,
            "deletedBy" to null
        )

        // Add a new document with a generated ID
        db.collection("highAndLow")
            .add(highAndLow)
            .addOnSuccessListener { documentReference ->
                Log.d(Ref.TAG_FIRESTORE, "highAndLow added with ID: ${documentReference.id}")
                hideProgressBar()
            }
            .addOnFailureListener { e ->
                Log.w(Ref.TAG_FIRESTORE, "Error adding document", e)
                hideProgressBar()
            }
    }

    private fun signOut() {
        auth.signOut()
        // updateUI(null)
    }

    /*
    private fun linkAccount() {
        // Make sure form is valid
        if (!validateLinkForm()) {
            return
        }

        // Get email and password from the form
        val email = fieldEmail.text.toString()
        val password = fieldPassword.text.toString()

        // Create EmailAuthCredential with email and password
        val credential = EmailAuthProvider.getCredential(email, password)

        // Link the anonymous user to the email credential
        showProgressDialog()

        // [START link_credential]
        auth.currentUser?.linkWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "linkWithCredential:success")
                    val user = task.result?.user
                    updateUI(user)
                } else {
                    Log.w(TAG, "linkWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // [START_EXCLUDE]
                hideProgressDialog()
                // [END_EXCLUDE]
            }
        // [END link_credential]
    }

    private fun validateLinkForm(): Boolean {
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            valid = false
        } else {
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = "Required."
            valid = false
        } else {
            fieldPassword.error = null
        }

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        val isSignedIn = user != null

        // Status text
        if (isSignedIn) {
            anonymousStatusId.text = getString(R.string.id_fmt, user!!.uid)
            anonymousStatusEmail.text = getString(R.string.email_fmt, user.email)
        } else {
            anonymousStatusId.setText(R.string.signed_out)
            anonymousStatusEmail.text = null
        }

        // Button visibility
        buttonAnonymousSignIn.isEnabled = !isSignedIn
        // buttonAnonymousSignOut.isEnabled = isSignedIn
        // buttonLinkAccount.isEnabled = isSignedIn
    }
    */

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
