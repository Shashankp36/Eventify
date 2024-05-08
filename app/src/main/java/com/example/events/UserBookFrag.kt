package com.example.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class UserBookFrag : Fragment() {
    private var databaseBook: DatabaseReference? = null
    var userID: String? = null
    var bookArrayList: ArrayList<BookingModel>? = null
    var bookAdapter: RecyclerUserBookAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_user_book, container, false)

        val usrBookRecycler = rootView.findViewById<RecyclerView>(R.id.usr_book_recycler)
        usrBookRecycler.setHasFixedSize(true)
        usrBookRecycler.layoutManager = LinearLayoutManager(context)
        bookArrayList = arrayListOf()
        userID = Firebase.auth.currentUser?.uid.toString()

        databaseBook = FirebaseDatabase.getInstance().getReference("bookings")
        val query = databaseBook!!.orderByChild("userID").equalTo(userID)

        val pkgValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        bookArrayList!!.clear()
                        for (snapshot in dataSnapshot.children) {
                            var bookItemModel = snapshot.getValue<BookingModel>()
                            bookArrayList!!.add(bookItemModel!!)
                        }
                        bookAdapter = RecyclerUserBookAdapter(context!!, bookArrayList!!)
                        usrBookRecycler.adapter = bookAdapter
                    }
                } catch (e: NullPointerException) {
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        query.addValueEventListener(pkgValueEventListener)
        return rootView
    }
}