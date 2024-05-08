package com.example.events

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class UserAccountFrag : Fragment() {
    private var databaseUsr: DatabaseReference? = null
    var userID:String?=null
    var user: FirebaseUser? = null

    private lateinit var usrNameAcc: TextView
    private lateinit var usrPhoneAcc: TextView
    private lateinit var viewUsrProfile: TextView
    private lateinit var changeAccPass: TextView
    private lateinit var logoutUsr: TextView
    private lateinit var textView23: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_account, container, false)

        usrNameAcc = view.findViewById(R.id.usr_nameAcc)
        usrPhoneAcc = view.findViewById(R.id.usr_PhoneAcc)
        viewUsrProfile = view.findViewById(R.id.viewUsrProfile)
        changeAccPass = view.findViewById(R.id.changeAccPass)
        logoutUsr = view.findViewById(R.id.logoutUsr)
        textView23 = view.findViewById(R.id.textView23)

        userID= Firebase.auth.currentUser?.uid.toString()
        user= FirebaseAuth.getInstance().currentUser
        databaseUsr = FirebaseDatabase.getInstance().getReference("users").child(userID!!)
        loadUsrDetails(databaseUsr!!,userID!!)

        logoutUsr.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val i = Intent(activity, LoginActivity::class.java)
            i.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
        textView23.setOnClickListener {
            startActivity(Intent(activity, AboutUs::class.java))
        }

        viewUsrProfile.setOnClickListener {
            val i = Intent(activity, UserEditProfile::class.java)
            i.putExtra("name",usrNameAcc.text.toString())
            i.putExtra("email",usrPhoneAcc.text.toString())
            startActivity(i)
        }

        return view
    }

    private fun loadUsrDetails(databaseUsr: DatabaseReference, userID: String) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue<UserModel>() != null) {
                    val key = dataSnapshot.key
                    if (key == userID) {
                        usrNameAcc.text = dataSnapshot.child("cname").value.toString()
                        usrPhoneAcc.text = dataSnapshot.child("email").value.toString()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("UserAccountFragment", "loadUsrDetails: onCancelled", databaseError.toException())
            }
        }

        databaseUsr.addValueEventListener(valueEventListener)
    }
}