package com.example.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class CompanyBookFrag : Fragment() {
    private var databaseBook: DatabaseReference? = null
    var userID: String? = null
    var bookArrayList: ArrayList<BookingModel>? = null
    var bookAdapter: RecyclerCmpBookAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_company_book, container, false)

        val cmpBookRecycler: RecyclerView = view.findViewById(R.id.cmp_book_recycler)
        cmpBookRecycler.setHasFixedSize(true)
        cmpBookRecycler.layoutManager = LinearLayoutManager(requireContext())
        bookArrayList = arrayListOf()
        userID = FirebaseAuth.getInstance().currentUser?.uid

        databaseBook = FirebaseDatabase.getInstance().getReference("bookings")
        val query = databaseBook!!.orderByChild("cmpID").equalTo(userID)

        val pkgValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        bookArrayList!!.clear()
                        for (snapshot in dataSnapshot.children) {
                            var bookItemModel = snapshot.getValue<BookingModel>()
                            bookArrayList!!.add(bookItemModel!!)
                        }
                        bookAdapter = RecyclerCmpBookAdapter(requireContext(), bookArrayList!!)
                        cmpBookRecycler.adapter = bookAdapter
                    }
                } catch (e: NullPointerException) {
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled event
            }
        }
        query.addValueEventListener(pkgValueEventListener)
        return view
    }
}