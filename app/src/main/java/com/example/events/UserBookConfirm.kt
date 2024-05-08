package com.example.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserBookConfirm : BaseActivity() {
    private lateinit var pkgNameConfirmTV: TextView
    private lateinit var pkgPriceConfirmTV: TextView
    private lateinit var pkgDescConfirmTV: TextView
    private lateinit var nPplConfirmTV: TextView
    private lateinit var usrConfirmBook: Button

    var databaseBook: DatabaseReference? = null
    var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_book_confirm)

        pkgNameConfirmTV = findViewById(R.id.pkgNameConfirmTV)
        pkgPriceConfirmTV = findViewById(R.id.pkgPriceConfirmTV)
        pkgDescConfirmTV = findViewById(R.id.pkgDescConfirmTV)
        nPplConfirmTV = findViewById(R.id.nPplConfirmTV)
        usrConfirmBook = findViewById(R.id.usr_confirm_book)

        pkgNameConfirmTV.text = intent.getStringExtra("pkgName")
        pkgPriceConfirmTV.text = "\u20B9" + intent.getStringExtra("pkgPrice")
        pkgDescConfirmTV.text = intent.getStringExtra("pkgDesc")
        nPplConfirmTV.text = intent.getStringExtra("pkgPpl")
        val title = intent.getStringExtra("pkgName") + " - " + intent.getStringExtra("pkgPpl") + " ppl"

        databaseBook = FirebaseDatabase.getInstance().getReference("bookings")

        usrConfirmBook.setOnClickListener {
            userID = FirebaseAuth.getInstance().currentUser!!.uid
            val bookID = databaseBook!!.push().key.toString()
            val bookingModel = BookingModel(
                bookID,
                intent.getStringExtra("packID"),
                intent.getStringExtra("cmpID"),
                userID,
                title,
                intent.getStringExtra("pkgPrice"),
                intent.getStringExtra("pkgDesc"),
                "wait"
            )
            databaseBook!!.child(bookID).setValue(bookingModel)
            Toast.makeText(applicationContext, "Request Sent! Waiting for Approval.", Toast.LENGTH_LONG).show()
            val i = Intent(applicationContext, UserDash::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
    }
}