package com.example.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CompanyRegistration : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_registration)

        auth = FirebaseAuth.getInstance()

        val cmpDetailsReg = findViewById<AppCompatButton>(R.id.cmp_details_reg)
        cmpDetailsReg.setOnClickListener {
            registerCompany()
        }
    }
    private fun registerCompany() {
        val companyName = findViewById<TextInputEditText>(R.id.cmp_reg_name).text.toString()
        val email = findViewById<TextInputEditText>(R.id.cmp_reg_mail).text.toString()
        val password = findViewById<TextInputEditText>(R.id.cmp_reg_pass).text.toString()
        val phone = findViewById<TextInputEditText>(R.id.cmp_reg_phone).text.toString()
        val doe = findViewById<TextInputEditText>(R.id.cmp_reg_doe).text.toString()
        val cin = findViewById<TextInputEditText>(R.id.cmp_reg_cin).text.toString()
        val description = findViewById<TextInputEditText>(R.id.cmp_reg_des).text.toString()

        val services = StringBuilder()
        val flo = findViewById<CheckBox>(R.id.flo)
        val cate = findViewById<CheckBox>(R.id.cate)
        val dec = findViewById<CheckBox>(R.id.dec)
        val all = findViewById<CheckBox>(R.id.all)

        if (flo.isChecked) {
            services.append("f")
        }
        if (cate.isChecked) {
            services.append("c")
        }
        if (dec.isChecked) {
            services.append("d")
        }
        if (all.isChecked) {
            services.append("a")
        }

        val selectedServices = services.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign up success, proceed with saving company details
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        val cmpID = currentUser.uid
                        if (cmpID.isNotEmpty()) {
                            // Save company details in Realtime Database
                            val database = FirebaseDatabase.getInstance()
                            val myRef = database.getReference("company").child(cmpID)
                            val companyData = CompanyModel(cmpID, phone, companyName, email, doe, cin, description, selectedServices, null)
                            myRef.setValue(companyData).addOnSuccessListener {
                                Toast.makeText(this, "Company registered successfully.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@CompanyRegistration, CompanyLoc::class.java)
                                startActivity(intent)
                            }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed to register company: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            // Handle empty company ID
                            Toast.makeText(this, "Failed to retrieve company ID.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle null current user
                        Toast.makeText(this, "Authentication failed: Current user is null.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}