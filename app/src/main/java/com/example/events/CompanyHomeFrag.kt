package com.example.events

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase

class CompanyHomeFrag : Fragment() {
    private lateinit var fabAddPackage: FloatingActionButton
    private var databaseCmp: DatabaseReference? = null
    private var databasePkg: DatabaseReference? = null
    var userID:String?=null
    var packageArrayList: ArrayList<PackageModel>? = null
    var homeAdapter:RecyclerCompanyHomeAdapter?=null
    private lateinit var cmpNameHomeTV: TextView
    private lateinit var locHomeTV: TextView
    private lateinit var packagerv:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_company_home, container, false)
        fabAddPackage = view.findViewById(R.id.fab_add_package)
        cmpNameHomeTV = view.findViewById(R.id.cmpNameHomeTV)
        locHomeTV = view.findViewById(R.id.locHomeTV)

        packagerv=view.findViewById(R.id.cmp_home_recycler)
        packagerv.layoutManager = LinearLayoutManager(requireContext())

        fabAddPackage.setOnClickListener {
            openAddCarActivity()
        }
        userID= Firebase.auth.currentUser?.uid.toString()
        packageArrayList= arrayListOf()
        databaseCmp = FirebaseDatabase.getInstance().getReference("company").child(userID!!)
        databasePkg = FirebaseDatabase.getInstance().getReference("packages")
        loadCompanyDetails(databaseCmp!!)

        val query = databasePkg!!.orderByChild("cmpID").equalTo(userID)
        val pkgValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (isAdded) { // Check if the fragment is attached
                    if (packageArrayList != null) {
                        try {
                            if (dataSnapshot.exists()) {
                                packageArrayList!!.clear()
                                for (snapshot in dataSnapshot.children) {
                                    val packageItemModel = snapshot.getValue<PackageModel>()
                                    if (packageItemModel != null) {
                                        packageArrayList!!.add(packageItemModel)
                                    }
                                }
                                homeAdapter = RecyclerCompanyHomeAdapter(requireContext(), packageArrayList!!)
                                packagerv.adapter = homeAdapter
                                val totalPackages = packageArrayList!!.size
                                Log.d(TAG, "Total number of packages added by the user: $totalPackages")
                            }
                        } catch (e: NullPointerException) {
                            Log.e(TAG, "Error: ${e.message}")
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Database Error: ${databaseError.message}")
            }
        }
        query.addValueEventListener(pkgValueEventListener)
        return view
    }

    private fun loadCompanyDetails(databaseCmp: DatabaseReference) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue<CompanyModel>() != null) {
                    val key = dataSnapshot.key
                    if (key == userID) {
                        cmpNameHomeTV.text = dataSnapshot.child("cname").value.toString()
                        locHomeTV.text = dataSnapshot.child("location").value.toString()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Database Error: ${databaseError.message}")
            }
        }
        databaseCmp.addValueEventListener(valueEventListener)
    }

    private fun openAddCarActivity() {
        val intent = Intent(activity, AddPackages::class.java)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "CompanyHomeFrag"
    }
}