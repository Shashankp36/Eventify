package com.example.events

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class UsercompnyDetails : BaseActivity() {
    var cmpID:String?=null
    var phone:String?=null
    private var databasePkg: DatabaseReference? = null
    var packageArrayList: ArrayList<PackageModel>? = null
    var homeAdapter: RecyclerUserCmpPackageAdapter?=null

    // Declare your views
    private lateinit var cmpNameHomeTV: TextView
    private lateinit var locHomeTV: TextView
    private lateinit var cmpMailHomeTV: TextView
    private lateinit var yoeHomeTV: TextView
    private lateinit var serviceHomeTV: TextView
    private lateinit var descHomeTV: TextView
    private lateinit var usrCall: ImageButton
    private lateinit var usrChat: ImageButton
    private lateinit var cmpHomeRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usercompny_details)

        // Initialize your views using findViewById
        cmpNameHomeTV = findViewById(R.id.cmpNameHomeTV)
        locHomeTV = findViewById(R.id.locHomeTV)
        cmpMailHomeTV = findViewById(R.id.cmpMailHomeTV)
        yoeHomeTV = findViewById(R.id.yoeHomeTV)
        serviceHomeTV = findViewById(R.id.serviceHomeTV)
        descHomeTV = findViewById(R.id.descHomeTV)
        usrCall = findViewById(R.id.usr_call)
        cmpHomeRecycler = findViewById(R.id.cmp_home_recycler)

        if(FirebaseAuth.getInstance().currentUser != null){
            usrCall.visibility= View.VISIBLE
        }
        else{
            usrCall.visibility= View.GONE
        }

        cmpID=intent.getStringExtra("cmpID")
        phone=intent.getStringExtra("phone")
        cmpNameHomeTV.text=intent.getStringExtra("cname")
        locHomeTV.text=intent.getStringExtra("location")
        cmpMailHomeTV.text=intent.getStringExtra("email")
        yoeHomeTV.text=intent.getStringExtra("yoe")
        var service=intent.getStringExtra("service")

        val sb = StringBuilder()
        if(service!!.contains("f")){
            sb.append("Florist\t")
        }
        if(service!!.contains("d")){
            sb.append("Decorator\t")
        }
        if(service!!.contains("c")){
            sb.append("Caterers\t")
        }
        if(service!!.contains("a")){
            sb.append("All.")
        }
        serviceHomeTV.text=sb.toString()

        descHomeTV.text=intent.getStringExtra("desc")

        usrCall.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$phone"))
            startActivity(i)
        }



        cmpHomeRecycler.setHasFixedSize(true)
        cmpHomeRecycler.layoutManager= LinearLayoutManager(this)
        packageArrayList= arrayListOf()
        databasePkg = FirebaseDatabase.getInstance().getReference("packages")
        val query = databasePkg!!.orderByChild("cmpID").equalTo(cmpID)

        val pkgValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    packageArrayList!!.clear()
                    for (snapshot in dataSnapshot.children) {
                        val packageItemModel = snapshot.getValue<PackageModel>()
                        if (packageItemModel != null) {
                            packageArrayList!!.add(packageItemModel)
                        }
                    }
                    homeAdapter = RecyclerUserCmpPackageAdapter(applicationContext, packageArrayList!!)
                    cmpHomeRecycler.adapter = homeAdapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        query.addValueEventListener(pkgValueEventListener)
    }
}