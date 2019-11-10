package com.itomakiweb.android.bank.pages

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.itomakiweb.android.bank.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var isClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTopFragment()

        isClicked = false

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
        val fragment = TopFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragment, fragment)
            .commit()
    }

    fun setMenuFragment() {
        if (isClicked) return

        val fragment = MenuFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragment, fragment)
            .addToBackStack(null)
            .commit()

        isClicked = true
    }
}
