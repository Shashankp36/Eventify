package com.example.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

class AddPackages : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseCmpPack: FirebaseDatabase
    private lateinit var cmp_pack_name: EditText
    private lateinit var cmp_pack_des: EditText
    private lateinit var cmp_price: EditText
    private lateinit var cmp_pCount: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_packages)

        // Initialize Firebase Authentication
        auth = Firebase.auth

        // Initialize Firebase Realtime Database
        databaseCmpPack = FirebaseDatabase.getInstance()

        // Find views and assign them to properties
        cmp_pack_name = findViewById(R.id.cmp_pack_name)
        cmp_pack_des = findViewById(R.id.cmp_pack_des)
        cmp_price = findViewById(R.id.cmp_price)
        cmp_pCount = findViewById(R.id.cmp_pCount)

        // Click listener for the "Add Package" button
        val addpackage: Button = findViewById(R.id.cmp_add_package)
        addpackage.setOnClickListener {
            if (checkEditText()) {
                val packID = databaseCmpPack.reference.child("packages").push().key.toString()
                val cmpID = auth.currentUser?.uid.toString() // Using current user's ID as cmpID
                val packageName = cmp_pack_name.text.toString()
                val packageDescription = cmp_pack_des.text.toString()
                val packagePrice = cmp_price.text.toString()
                val packagePeopleCount = cmp_pCount.text.toString()

                val packageItemModel = PackageModel(cmpID, packID, packageName, packageDescription, packagePrice, packagePeopleCount)
                databaseCmpPack.reference.child("packages").child(packID).setValue(packageItemModel)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Package added successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, CompanyDash::class.java)
                        startActivity(intent)

                        finish()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Failed to add package: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun checkEditText(): Boolean {
        var status = true
        if (cmp_pack_name.text.toString().isEmpty()) {
            findViewById<TextInputLayout>(R.id.cmp_name_layout).error = "Check Fields & Try Again"
            status = false
        }
        if (cmp_pCount.text.toString().isEmpty()) {
            findViewById<TextInputLayout>(R.id.cmp_doe_layout).error = "Check Fields & Try Again"
            status = false
        }
        if (cmp_price.text.toString().isEmpty()) {
            findViewById<TextInputLayout>(R.id.cmp_cin_layout).error = "Check Fields & Try Again"
            status = false
        }
        if (cmp_pack_des.text.toString().isEmpty()) {
            findViewById<TextInputLayout>(R.id.cmp_des_layout).error = "Check Fields & Try Again"
            status = false
        }
        return status
    }
}
