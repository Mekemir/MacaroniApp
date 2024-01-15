package com.game.macaroniapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.game.macaroniapp.databinding.ActivityMainBinding
import com.game.macaroniapp.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var musicIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_layout, HomeFragment(), "HomeFragment::class.java")
                .commitNow()
        }
    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager.beginTransaction()
            .replace(R.id.drawer_layout, HomeFragment(), "HomeFragment::class.java")
            .commitNow()
    }

    override fun onBackPressed() {
        val popBackSize: Int = supportFragmentManager.fragments.size
        if (popBackSize == 1) {
            super.onBackPressed()
        }
        val popFragment = supportFragmentManager.fragments[popBackSize - 1]
        supportFragmentManager.beginTransaction().remove(popFragment).commit()
    }
}