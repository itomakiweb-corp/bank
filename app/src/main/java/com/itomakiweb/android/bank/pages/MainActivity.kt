package com.itomakiweb.android.bank.pages

import android.os.Bundle
import android.util.Log
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.VisibleForTesting
import com.google.firebase.firestore.FirebaseFirestore
import com.itomakiweb.android.bank.R
import kotlinx.android.synthetic.main.activity_main.*
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Firebase Auth
        auth = FirebaseAuth.getInstance()
        /*
        buttonAnonymousSignIn.setOnClickListener {
            signInAnonymously()
        }

         */
        buttonAnonymousSignOut.setOnClickListener {
            signOut()
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

        mainFragment.setOnClickListener {
            setMenuFragment()
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

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.d(TAG, "signInAnonymously:begin")
            signInAnonymously()
        }
        // updateUI(currentUser)
    }
    // [END on_start_check_user]

    private fun signInAnonymously() {
        showProgressDialog()
        // [START signin_anonymously]
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInAnonymously:success")
                    val currentUser = auth.currentUser!!
                    // Log.d(TAG, user.toString())
                    createUser(currentUser)
                    // updateUI(user)
                    createHighAndLow(currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    // updateUI(null)
                }

                // [START_EXCLUDE]
                hideProgressDialog()
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
            "name" to "TODO",
            "moneyTotalCurrent" to 80000,
            "moneyOwnCurrent" to 0,
            "moneyBorrowCurrent" to 80000,
            "createdAt" to FieldValue.serverTimestamp(),
            "createdBy" to currentUser.uid,
            "updatedAt" to FieldValue.serverTimestamp(),
            "updatedBy" to currentUser.uid
        )

        // Add a new document with a generated ID
        db.collection("users")
            .document(currentUser.uid)
            .set(user)
            .addOnSuccessListener {
                Log.d("test", "DocumentSnapshot added with ID: ${currentUser.uid}")
            }
            .addOnFailureListener { e ->
                Log.w("test", "Error adding document", e)
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
            "createdAt" to FieldValue.serverTimestamp(),
            "createdBy" to currentUser.uid,
            "updatedAt" to FieldValue.serverTimestamp(),
            "updatedBy" to currentUser.uid
        )

        val highAndLowSubCollection = hashMapOf(
            "numberSet" to 0,
            "countGame" to 0,
            "countGameMax" to 10,
            "moneyBetRateGames" to 1000
        )

        highAndLow["sets"] = highAndLowSubCollection

        // Add a new document with a generated ID
        db.collection("highAndLow")
            .add(highAndLow)
            .addOnSuccessListener { documentReference ->
                Log.d("test", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("test", "Error adding document", e)
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

    companion object {
        private const val TAG = "AnonymousAuth"
    }

    @VisibleForTesting
    val progressDialog by lazy {
        ProgressDialog(this)
    }

    fun showProgressDialog() {
        progressDialog.setMessage(getString(R.string.loading))
        progressDialog.isIndeterminate = true
        progressDialog.show()
    }

    fun hideProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    public override fun onStop() {
        super.onStop()
        hideProgressDialog()
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
