package com.example.events

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth


class CompanyAccFrag : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_company_acc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.logoutCmp).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val i = Intent(activity, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
        view.findViewById<View>(R.id.viewCmpProfile).setOnClickListener {
            startActivity(Intent(activity, CompanyEditProfile::class.java))
        }
        view.findViewById<View>(R.id.textView23).setOnClickListener {
            startActivity(Intent(activity, AboutUs::class.java))
        }
    }
}