package com.itomakiweb.android.bank.pages


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

import com.itomakiweb.android.bank.R
import kotlinx.android.synthetic.main.fragment_high_and_low_play.*

/**
 * A simple [Fragment] subclass.
 */
class HighAndLowPlayFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_high_and_low_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Auth
        auth = FirebaseAuth.getInstance()

        highButton.setOnClickListener {
            val fragment = HighAndLowResultFragment()
            fragmentManager!!.beginTransaction()
                .replace(R.id.highAndLowFragmentFrame, fragment)
                .addToBackStack(null)
                .commit()
        }

        lowButton.setOnClickListener {
            val fragment = HighAndLowResultFragment()
            fragmentManager!!.beginTransaction()
                .replace(R.id.highAndLowFragmentFrame, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser!!

        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .whereEqualTo("uid", currentUser.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    //TODO: ベット金額は仮に1000を入れているので修正が必要
                    document.reference.update("moneyTotalCurrent", FieldValue.increment(-1000))
                    document.reference.update("moneyOwnCurrent", FieldValue.increment(-1000))
                    Log.d("get", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("get", "Error getting documents.", exception)
            }

        db.collection("highAndLow")
            .whereEqualTo("createdBy", currentUser.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    //TODO: sets.countGameとgames関連の修正が必要
                    document.reference.update("countGameTotalSets", FieldValue.increment(1))
                    Log.d("get", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("get", "Error getting documents.", exception)
            }

    }


}
