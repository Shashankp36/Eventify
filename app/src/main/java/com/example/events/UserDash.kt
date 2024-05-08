package com.example.events

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserDash : BaseActivity() {
    lateinit var  bottomNavigationView: BottomNavigationView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dash)
        bottomNavigationView = findViewById(R.id.ubottom_navigation_view)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    replaceFragment(UserHomeFrag())
                    true
                }
                R.id.nav_book -> {
                    replaceFragment(UserBookFrag())
                    true
                }
                R.id.nav_account -> {
                    replaceFragment(UserAccountFrag())
                    true
                }
                else -> false
            }
        }
        replaceFragment(UserHomeFrag())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}