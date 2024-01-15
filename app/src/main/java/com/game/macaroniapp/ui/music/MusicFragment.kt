package com.game.macaroniapp.ui.music

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.game.macaroniapp.CookingItemData
import com.game.macaroniapp.MainActivity
import com.game.macaroniapp.Player
import com.game.macaroniapp.R
import com.game.macaroniapp.databinding.MusicFragmentBinding

class MusicFragment: Fragment() {
    private var binding: MusicFragmentBinding? = null
    private lateinit var viewModel: MusicViewModel
    var isTurnedOff = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MusicViewModel::class.java)
        isTurnedOff = (activity as MainActivity).musicIndex == 0
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.music_fragment, container, false)
        binding?.lifecycleOwner = this.viewLifecycleOwner

        binding?.data = viewModel

        binding?.songOne?.setOnClickListener {
            (activity as? MainActivity)?.stopSong()
            (activity as? MainActivity)?.musicIndex = 1
            (activity as? MainActivity)?.playSong()
            binding?.turnOn?.setImageDrawable(this.context?.getDrawable(R.drawable.songicon))
            isTurnedOff = false
        }
        binding?.songTwo?.setOnClickListener {
            (activity as? MainActivity)?.stopSong()
            (activity as? MainActivity)?.musicIndex = 2
            (activity as? MainActivity)?.playSong()
            binding?.turnOn?.setImageDrawable(this.context?.getDrawable(R.drawable.songicon))
            isTurnedOff = false
        }
        binding?.songThree?.setOnClickListener {
            (activity as? MainActivity)?.stopSong()
            (activity as? MainActivity)?.musicIndex = 3
            (activity as? MainActivity)?.playSong()
            binding?.turnOn?.setImageDrawable(this.context?.getDrawable(R.drawable.songicon))
            isTurnedOff = false
        }
        if (isTurnedOff) {
            binding?.turnOn?.setImageDrawable(this.context?.getDrawable(R.drawable.turnedoff))
        } else {
            binding?.turnOn?.setImageDrawable(this.context?.getDrawable(R.drawable.songicon))
        }

        binding?.turnOn?.setOnClickListener {
            if (!isTurnedOff) {
                binding?.turnOn?.setImageDrawable(this.context?.getDrawable(R.drawable.turnedoff))
                (activity as MainActivity).musicIndex = 0
                (activity as? MainActivity)?.stopSong()
                isTurnedOff = !isTurnedOff
            }
        }
        binding?.homeBtn?.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding?.root
    }

    override fun onPause() {
        super.onPause()
        if (isTurnedOff) {
            (activity as MainActivity).musicIndex = 0
            (activity as MainActivity).stopSong()
        }
    }
}