package com.example.events

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class CompanyDash : BaseActivity() {
    lateinit var  bottomNavigationView: BottomNavigationView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_dash)
        bottomNavigationView = findViewById(R.id.cbottom_navigation_view)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    replaceFragment(CompanyHomeFrag())
                    true
                }
                R.id.nav_book -> {
                    replaceFragment(CompanyBookFrag())
                    true
                }
                R.id.nav_setting -> {
                    replaceFragment(CompanyAccFrag())
                    true
                }
                else -> false
            }
        }
        replaceFragment(CompanyHomeFrag())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}