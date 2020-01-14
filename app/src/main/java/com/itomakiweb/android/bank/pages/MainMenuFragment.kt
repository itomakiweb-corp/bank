package com.itomakiweb.android.bank.pages


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.itomakiweb.android.bank.R
import com.itomakiweb.android.bank.libraries.Cloud
import com.itomakiweb.android.bank.libraries.Ref
import kotlinx.android.synthetic.main.fragment_main_menu.*

/**
 * A simple [Fragment] subclass.
 */
class MainMenuFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Auth
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance()

        whenAdminUser()

        /**toBlackJack.setOnClickListener {
            val intent = Intent(activity, BlackJackActivity::class.java)
            startActivity(intent)
        }*/

        toHighAndLow.setOnClickListener {
            val intent = Intent(activity, HighAndLowActivity::class.java)
            startActivity(intent)
        }

        toStaffRole.setOnClickListener {
            val intent = Intent(activity, StaffRoleActivity::class.java)
            startActivity(intent)
        }

        toQuestNew.setOnClickListener {
            val intent = Intent(activity, QuestNewActivity::class.java)
            startActivity(intent)
        }
    }

    fun whenAdminUser() {
        db.collection(Cloud.usersCollectionPath)
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { user ->
                val role = user["role"] as String
                if(role == "admin") {
                    toStaffRole.visibility = View.VISIBLE
                    toQuestNew.visibility = View.VISIBLE
                }
                Log.d(Ref.TAG_FIRESTORE, "${user.id} => ${user.data}")

            }
            .addOnFailureListener { exception ->
                Log.w(Ref.TAG_FIRESTORE, "Error getting documents.", exception)
            }

    }
}
