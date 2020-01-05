package com.itomakiweb.android.bank.libraries

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
        val progressBar = ProgressBar(this).apply { id = View.generateViewId() }
        val constraintLayout = findViewById<ConstraintLayout>(R.id.layout)
        constraintLayout.addView(progressBar)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            progressBar.id,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT
        )
        constraintSet.connect(
            progressBar.id,
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT
        )
        constraintSet.connect(
            progressBar.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            progressBar.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )

        constraintSet.applyTo(constraintLayout)

        //val progressBarLayoutParams = ConstraintLayout.LayoutParams(100, 100)
        //progressBarLayoutParams.layoutDirection =

        //layout.addView(progressBar, progressBarLayoutParams)

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

    fun hideProgressBar() {
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
        hideProgressBar()
    }


    // for coroutine
    override fun onDestroy() {
        super.onDestroy()
        cancel() // CoroutineScope.cancel
    }
}
