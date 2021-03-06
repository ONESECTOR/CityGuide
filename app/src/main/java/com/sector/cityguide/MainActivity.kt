package com.sector.cityguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sector.cityguide.databinding.ActivityMainBinding
import com.sector.cityguide.main.MyApplication

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_CityGuide)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigationView()
        initFirebase()
    }

    private fun setBottomNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(
            R.id.bottom_navigation_view
        )

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragment
        ) as NavHostFragment

        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.homeFragment -> bottomNavigationView.visibility = View.VISIBLE
                R.id.favoriteFragment -> bottomNavigationView.visibility = View.VISIBLE
                R.id.profileFragment -> bottomNavigationView.visibility = View.VISIBLE
                else -> bottomNavigationView.visibility = View.GONE
            }
        }

        bottomNavigationView.setupWithNavController(navController)
    }

    private fun initFirebase() {
        MyApplication(this).initFirebase()
    }
}
