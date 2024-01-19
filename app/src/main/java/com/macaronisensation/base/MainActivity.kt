package com.macaronisensation.base

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.macaronisensation.R
import com.macaronisensation.databinding.ActivityMainBinding
import com.macaronisensation.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var musicIndex: Int = 0
    var player: MediaPlayer? = null


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
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.drawer_layout, HomeFragment(), "HomeFragment::class.java")
//            .commitNow()
        playSong()
    }

    override fun onPause() {
        super.onPause()
        stopSong()
    }

    override fun onBackPressed() {
        val popBackSize: Int = supportFragmentManager.fragments.size
        if (popBackSize == 1) {
            super.onBackPressed()
        }
        val popFragment = supportFragmentManager.fragments[popBackSize - 1]
        supportFragmentManager.beginTransaction().remove(popFragment).commit()
    }

    fun playSong() {
        player = Player().play(musicIndex, this)
    }

    fun stopSong() {
        player?.stop()
    }
}