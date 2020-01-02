package com.itomakiweb.android.bank.libraries

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.itomakiweb.android.bank.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * should use when async
 */
abstract class ScopedAppActivity: AppCompatActivity(), CoroutineScope by MainScope() {

    // for progress bar
    @VisibleForTesting
    val progressDialog by lazy {
        val progressBar = ProgressBar(this)
        val layout = findViewById<ViewGroup>(R.id.layout)
        val progressBarLayoutParams = RelativeLayout.LayoutParams(100, 100)
        // progressBarLayoutParams.gravity = Gravity.CENTER
        progressBarLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        progressBar.layoutParams = progressBarLayoutParams

        layout.addView(progressBar)

        progressBar
        // ProgressDialog(this)
    }

    fun showProgressDialog() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        progressDialog.visibility = View.VISIBLE

        /*
        progressDialog.setMessage(getString(R.string.loading))
        progressDialog.isIndeterminate = true
        progressDialog.show()
         */
    }

    fun hideProgressDialog() {
        progressDialog.visibility = View.GONE

        window.clearFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        /*

        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
         */
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    public override fun onStop() {
        super.onStop()
        hideProgressDialog()
    }


    // for coroutine
    override fun onDestroy() {
        super.onDestroy()
        cancel() // CoroutineScope.cancel
    }
}
