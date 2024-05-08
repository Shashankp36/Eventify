package com.example.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRegistration : BaseActivity() {
    private lateinit var usrSignName: TextView
    private lateinit var usrSignMail: TextView
    private lateinit var usrSignPass: TextView
    private lateinit var nameLayout: TextInputLayout
    private lateinit var usrMailLayout: TextInputLayout
    private lateinit var usrPassLayout: TextInputLayout
    private lateinit var usrSignup: AppCompatButton

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseUsr: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)

        usrSignName = findViewById(R.id.usr_sign_name)
        usrSignMail = findViewById(R.id.usr_sign_mail)
        usrSignPass = findViewById(R.id.usr_sign_pass)
        nameLayout = findViewById(R.id.name_layout)
        usrMailLayout = findViewById(R.id.usr_mail_layout)
        usrPassLayout = findViewById(R.id.usr_pass_layout)
        usrSignup = findViewById(R.id.usr_signup)

        usrSignup.setOnClickListener {
            val name = usrSignName.text.toString()
            val email = usrSignMail.text.toString()
            val pass = usrSignPass.text.toString()
            if (checkEditText()) {
                mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            userID = mAuth.currentUser!!.uid
                            val userModel = UserModel(userID, name, email)
                            databaseUsr.child(userID!!).setValue(userModel)
                            val i = Intent(this@UserRegistration, UserDash::class.java)
                            i.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(i)
                        } else {
                            Toast.makeText(
                                this@UserRegistration,
                                "Error !" + task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    fun onCmpSignUp(view: View) {
        startActivity(Intent(this@UserRegistration, CompanyRegistration::class.java))
    }

    fun onUsrLoginRegClick(view: View) {
        startActivity(Intent(this@UserRegistration, LoginActivity::class.java))
    }

    private fun checkEditText(): Boolean {
        var status = true
        if (usrSignName.text.toString().isEmpty()) {
            nameLayout.error = "Check Fields & Try Again"
            status = false
        }
        if (usrSignMail.text.toString().isEmpty()) {
            usrMailLayout.error = "Check Fields & Try Again"
            status = false
        }
        if (usrSignPass.text.toString().isEmpty()) {
            usrPassLayout.error = "Check Fields & Try Again"
            status = false
        }
        return status
    }
}