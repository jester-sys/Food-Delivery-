package com.krishna.foodwave.Admin.UI

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.krishna.foodwave.R
import com.krishna.foodwave.databinding.ActivityAdminHomeBinding

class AdminHomeActivity : AppCompatActivity() {
    private  lateinit var binding:ActivityAdminHomeBinding
    private lateinit var customBottomNavigation: BottomNavigationView
    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        if (savedInstanceState==null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_view,AdminHomeFragment())
                .commit()
        }
        customBottomNavigation = findViewById(R.id.customBottomNavigation)
        customBottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, AdminHomeFragment())
                        .commit()
                    true
                }
                R.id.Category -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, CategoryFragment())
                        .commit()
                    true
                }
                R.id.Product -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, ProductFragment())
                        .commit()
                    true
                }
                R.id.Slider -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view,SliderFragment())
                        .commit()
                    true
                }
                R.id.Oder -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view,AllOderDetailsFragment())
                        .commit()
                    true
                }
                else -> {
                false}
            }
            }

    }
}