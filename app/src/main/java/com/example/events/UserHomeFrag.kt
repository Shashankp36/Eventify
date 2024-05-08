package com.example.events

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.util.Locale


class UserHomeFrag : Fragment() {
    private lateinit var usrHomeRecycler: RecyclerView
    private lateinit var searchHome: EditText
    private lateinit var filterButton: ImageButton
    private var databaseCmp: DatabaseReference? = null
    private var userID: String? = null
    private var cmpArrayList: ArrayList<CompanyModel>? = null
    private var homeAdapter: RecyclerUserHomeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_home, container, false)

        usrHomeRecycler = view.findViewById(R.id.user_home_recycler)
        searchHome = view.findViewById(R.id.search_home)
        filterButton = view.findViewById(R.id.filter)

        usrHomeRecycler.setHasFixedSize(true)
        usrHomeRecycler.layoutManager = LinearLayoutManager(context)
        cmpArrayList = arrayListOf()
        databaseCmp = FirebaseDatabase.getInstance().getReference("company")
        loadDataFromDatabase()

        filterButton.setOnClickListener {
            showFilterDialog()
        }

        searchHome.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                search(s.toString())
            }
        })

        return view
    }
    private fun showFilterDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val alertView: View = layoutInflater.inflate(R.layout.filter_company, null)
        builder.setView(alertView)
        val cancel: AppCompatButton = alertView.findViewById(R.id.btn_filter_cancel)
        val filterCmp: AppCompatButton = alertView.findViewById(R.id.btn_filter)
        val flo: CheckBox = alertView.findViewById(R.id.floFilt)
        val cate: CheckBox = alertView.findViewById(R.id.cateFilt)
        val none: CheckBox = alertView.findViewById(R.id.noneFilt)
        val all: CheckBox = alertView.findViewById(R.id.allFilt)
        val dec: CheckBox = alertView.findViewById(R.id.decFilt)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.setGravity(Gravity.CENTER)
        alertDialog.show()
        all.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                flo.isChecked = true
                cate.isChecked = true
                dec.isChecked = true
                none.isChecked = false
            } else {
                flo.isChecked = false
                cate.isChecked = false
                dec.isChecked = false
                none.isChecked = false
            }
        }
        none.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                flo.isChecked = false
                cate.isChecked = false
                dec.isChecked = false
                all.isChecked = false
            }
        }
        cancel.setOnClickListener {
            alertDialog.dismiss()
        }
        filterCmp.setOnClickListener {
            val sb = StringBuilder()
            if (flo.isChecked) {
                sb.append("f")
            }
            if (cate.isChecked) {
                sb.append("c")
            }
            if (dec.isChecked) {
                sb.append("d")
            }
            if (all.isChecked) {
                sb.append("a")
            }
            if (none.isChecked) {
                loadDataFromDatabase()
            }
            val services = sb.toString()
            filter(services)
            alertDialog.dismiss()
        }
    }

    private fun search(string: String) {
        val searchList: ArrayList<CompanyModel> = arrayListOf()
        for (objectList in cmpArrayList!!) {
            if (objectList.cname.toString().lowercase(Locale.getDefault())
                    .contains(string.lowercase(Locale.getDefault()))
            ) {
                searchList.add(objectList)
            }
        }
        homeAdapter = RecyclerUserHomeAdapter(requireContext(), searchList)
        usrHomeRecycler.adapter = homeAdapter
    }

    private fun filter(string: String) {
        val filterList: ArrayList<CompanyModel> = arrayListOf()
        for (objectList in cmpArrayList!!) {
            if (objectList.service.toString().lowercase(Locale.getDefault())
                    .contains(string.lowercase(Locale.getDefault()))
            ) {
                filterList.add(objectList)
            }
        }
        homeAdapter = RecyclerUserHomeAdapter(requireContext(), filterList)
        usrHomeRecycler.adapter = homeAdapter
    }

    private fun loadDataFromDatabase() {
        if (cmpArrayList!!.isNotEmpty())
            cmpArrayList!!.clear()
        databaseCmp!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            val companyModel = postSnapshot.getValue<CompanyModel>()
                            cmpArrayList!!.add(companyModel!!)
                        }
                        homeAdapter = RecyclerUserHomeAdapter(context!!, cmpArrayList!!)
                        usrHomeRecycler.setAdapter(homeAdapter)
                    }
                } catch (e: NullPointerException) {
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}