package com.macaronsensationni.macaroniapp.ui.music

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.macaronsensationni.macaroniapp.MainActivity
import com.macaronsensationni.macaroniapp.R
import com.macaronsensationni.macaroniapp.databinding.MusicFragmentBinding
import com.macaronsensationni.macaroniapp.preferences.PreferencesRepository

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
        this.activity?.applicationContext?.let {
            viewModel.preferencesRepository = PreferencesRepository.getInstance(it)
            viewModel.musicIndex = viewModel.preferencesRepository?.numberOfAllAnswersFlow
        }

        binding?.data = viewModel

        binding?.songOne?.setOnClickListener {
            (activity as? MainActivity)?.stopSong()
            (activity as? MainActivity)?.musicIndex = 1
            (activity as? MainActivity)?.playSong()
            binding?.turnOn?.setImageDrawable(this.context?.getDrawable(R.drawable.songicon))
            viewModel.newSongChoosen(1)
            isTurnedOff = false
        }
        binding?.songTwo?.setOnClickListener {
            (activity as? MainActivity)?.stopSong()
            (activity as? MainActivity)?.musicIndex = 2
            viewModel.newSongChoosen(2)
            (activity as? MainActivity)?.playSong()
            binding?.turnOn?.setImageDrawable(this.context?.getDrawable(R.drawable.songicon))
            isTurnedOff = false
        }
        binding?.songThree?.setOnClickListener {
            (activity as? MainActivity)?.stopSong()
            (activity as? MainActivity)?.musicIndex = 3
            viewModel.newSongChoosen(3)
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
                viewModel.newSongChoosen(0)
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
            viewModel.newSongChoosen(0)
            (activity as MainActivity).stopSong()
        }
    }
}