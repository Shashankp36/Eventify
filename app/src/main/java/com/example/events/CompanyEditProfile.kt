package com.example.events

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.util.Locale

class CompanyEditProfile : BaseActivity() {
    private var databaseCmp: DatabaseReference? = null
    var userID: String? = null
    var service: String? = null
    var phone: String? = null
    var location: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationet:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_edit_profile)
        val submit =findViewById<AppCompatButton>(R.id.cmp_submit)
        val getlocation=findViewById<AppCompatButton>(R.id.get_curLoc)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
         locationet=findViewById(R.id.cmp_loc_reg)

        userID = FirebaseAuth.getInstance().currentUser?.uid
        databaseCmp = FirebaseDatabase.getInstance().getReference("company").child(userID!!)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.getValue<CompanyModel>()?.let { companyModel ->
                    if (dataSnapshot.key == userID) {
                        findViewById<TextInputEditText>(R.id.cmp_edit_name).setText(companyModel.cname)
                        findViewById<TextInputEditText>(R.id.cmp_edit_mail).setText(companyModel.email)
                        findViewById<TextInputEditText>(R.id.cmp_edit_doe).setText(companyModel.yoe)
                        findViewById<TextInputEditText>(R.id.cmp_edit_cin).setText(companyModel.cin)
                        findViewById<TextInputEditText>(R.id.cmp_edit_des).setText(companyModel.desc)
                        phone = companyModel.phone
                        location = companyModel.location
                        service = companyModel.service

                        findViewById<CheckBox>(R.id.flo).isChecked = service!!.contains("f")
                        findViewById<CheckBox>(R.id.cate).isChecked = service!!.contains("c")
                        findViewById<CheckBox>(R.id.dec).isChecked = service!!.contains("d")
                        findViewById<CheckBox>(R.id.all).isChecked = service!!.contains("a")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled
            }
        }

        databaseCmp!!.addValueEventListener(valueEventListener)

        findViewById<CheckBox>(R.id.all).setOnCheckedChangeListener { _, isChecked ->
            findViewById<CheckBox>(R.id.flo).isChecked = isChecked
            findViewById<CheckBox>(R.id.cate).isChecked = isChecked
            findViewById<CheckBox>(R.id.dec).isChecked = isChecked
        }
        getlocation.setOnClickListener{
            fetchCurrentLocation()
        }

        submit.setOnClickListener {
            val descEditText = findViewById<TextInputEditText>(R.id.cmp_edit_des)
            val newDesc = descEditText.text.toString()

            val locEditText = findViewById<TextInputEditText>(R.id.cmp_loc_reg)
            val newLoc = locEditText.text.toString()

            val sb = StringBuilder()
            sb.append(if (findViewById<CheckBox>(R.id.flo).isChecked) "f" else "")
            sb.append(if (findViewById<CheckBox>(R.id.cate).isChecked) "c" else "")
            sb.append(if (findViewById<CheckBox>(R.id.dec).isChecked) "d" else "")
            sb.append(if (findViewById<CheckBox>(R.id.all).isChecked) "a" else "")
            val services = sb.toString()

            val updateMap = HashMap<String, Any>()
            updateMap["desc"] = newDesc
            updateMap["service"] = services
            updateMap["location"] = newLoc

            userID?.let { userId ->
                databaseCmp?.updateChildren(updateMap)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Database update successful
                        Toast.makeText(this@CompanyEditProfile, "Update done", Toast.LENGTH_SHORT).show()
                    } else {
                        // Database update failed
                        // Handle the error, if necessary
                    }
                }
            }
        }
    }

    private fun fetchCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
                        locationet.setText(list[0].getAddressLine(0).toString())
                    } else {
                        Toast.makeText(this, "Unable to retrieve current location", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to retrieve current location: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    fetchCurrentLocation()
                } else {
                    Toast.makeText(this, "Permission denied. Unable to retrieve current location", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
    companion object {
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100
    }
}