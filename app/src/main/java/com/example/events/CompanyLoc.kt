package com.example.events

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale

class CompanyLoc : AppCompatActivity() {
    private lateinit var locationEditText: EditText
    private lateinit var getLocation:Button
    private lateinit var submit:Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val database = FirebaseDatabase.getInstance()
    private val userID = FirebaseAuth.getInstance().currentUser?.uid
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_loc)
        locationEditText=findViewById(R.id.edit_location)
        getLocation = findViewById(R.id.get_currentLoc)
        submit = findViewById(R.id.submit)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation.setOnClickListener {
            fetchCurrentLocation()
        }
        submit.setOnClickListener {
            saveLocationToDatabase()
        }
    }

    private fun saveLocationToDatabase() {
        val location = locationEditText.text.toString()
        if (location.isNotEmpty()) {
            userID?.let { uid ->
                database.reference.child("company").child(uid).child("location")
                    .setValue(location)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Location saved successfully", Toast.LENGTH_SHORT).show()
                        val i = Intent(this@CompanyLoc, LoginActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to save location: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        } else {
            Toast.makeText(this, "Location is empty", Toast.LENGTH_SHORT).show()
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
                        locationEditText.setText(list[0].getAddressLine(0).toString())
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