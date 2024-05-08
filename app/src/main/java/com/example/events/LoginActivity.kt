package com.example.events

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : BaseActivity() {
    private lateinit var tabs: TabLayout
    private lateinit var userLogin: View
    private lateinit var cmpLogin: View
    private lateinit var usrLoginMain: View
    private lateinit var cmpLoginMain: View
    private lateinit var logMail: TextView
    private lateinit var logPass: TextView
    private lateinit var logCmail: TextView
    private lateinit var logCpass: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        tabs = findViewById(R.id.tabs)
        userLogin = findViewById(R.id.user_login)
        cmpLogin = findViewById(R.id.cmp_login)
        usrLoginMain = findViewById(R.id.usr_login_main)
        cmpLoginMain = findViewById(R.id.cmp_login_main)
        logMail = findViewById(R.id.log_mail)
        logPass = findViewById(R.id.log_pass)
        logCmail = findViewById(R.id.log_cmail)
        logCpass = findViewById(R.id.log_cpass)

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        userLogin.visibility = View.VISIBLE
                        cmpLogin.visibility = View.GONE
                    }
                    1 -> {
                        userLogin.visibility = View.GONE
                        cmpLogin.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        usrLoginMain.setOnClickListener {
            loginUser()
        }

        cmpLoginMain.setOnClickListener {
            loginCompany()
        }
    }

    private fun loginUser() {
        val email = logMail.text.toString()
        val password = logPass.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Authenticate user with Firebase Authentication
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            isUserEmailExistsInDatabase(email)
                        } else {
                            Toast.makeText(
                                this@LoginActivity, "User is null.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity, "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(
                this@LoginActivity, "Email and password cannot be empty.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loginCompany() {
        val email = logCmail.text.toString()
        val password = logCpass.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            isCompanyEmailExistsInDatabase(email)
                        } else {
                            Toast.makeText(
                                this@LoginActivity, "User is null.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(
                this@LoginActivity, "Email and password cannot be empty.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isUserEmailExistsInDatabase(email: String) {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        val query = usersRef.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    loginUserWithEmail(email)
                } else {
                    Toast.makeText(
                        this@LoginActivity, "Invalid user login.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Error handling
            }
        })
    }

    private fun isCompanyEmailExistsInDatabase(email: String) {
        val companiesRef = FirebaseDatabase.getInstance().getReference("company")
        val query = companiesRef.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    loginCompanyWithEmail(email)
                } else {
                    Toast.makeText(
                        this@LoginActivity, "Invalid company login.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Error handling
            }
        })
    }

    private fun loginUserWithEmail(email: String) {
        // Navigate to User Home Activity
        val intent = Intent(this, UserDash::class.java)
        startActivity(intent)
    }

    private fun loginCompanyWithEmail(email: String) {
        // Navigate to Company Home Activity
        val intent = Intent(this, CompanyDash::class.java)
        startActivity(intent)
    }

    fun onSignUpClick(view: View) {
        val selectedTab = tabs.selectedTabPosition
        if (selectedTab == 0) {
            startActivity(Intent(this, UserRegistration::class.java))
        } else {
            startActivity(Intent(this, CompanyRegistration::class.java))
        }
    }
}