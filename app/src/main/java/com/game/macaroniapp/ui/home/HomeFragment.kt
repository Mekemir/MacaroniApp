package com.game.macaroniapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.game.macaroniapp.MainActivity
import com.game.macaroniapp.R
import com.game.macaroniapp.databinding.FragmentHomeBinding
import com.game.macaroniapp.preferences.PreferencesRepository
import com.game.macaroniapp.ui.aboutpasta.AboutPastaFragment
import com.game.macaroniapp.ui.cooking.intro.CookingIntroFragment
import com.game.macaroniapp.ui.music.MusicFragment
import com.game.macaroniapp.ui.music.MusicViewModel
import com.game.macaroniapp.ui.mydishes.MyDishesFragment
import com.game.macaroniapp.ui.recipes.RecipesFragment

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding?.lifecycleOwner = this.viewLifecycleOwner
        this.activity?.applicationContext?.let {
            viewModel.preferencesRepository = PreferencesRepository.getInstance(it)
            viewModel.musicIndex = viewModel.preferencesRepository?.numberOfAllAnswersFlow
        }

        binding?.data = viewModel

        val root: View? = binding?.root

        binding?.cook?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.drawer_layout, CookingIntroFragment(), "CookingIntroFragment::class.java")
                ?.commit()
        }

        binding?.aLittleAbout?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.drawer_layout, AboutPastaFragment(), "AboutPastaFragment::class.java")
                ?.commit()
        }

        binding?.recipes?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.drawer_layout, RecipesFragment(), "RecipesFragment::class.java")
                ?.commit()
        }

        binding?.myDishes?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.drawer_layout, MyDishesFragment(), "MyDishesFragment::class.java")
                ?.commit()
        }
        binding?.music?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.drawer_layout, MusicFragment(), "MusicFragment::class.java")
                ?.commit()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.musicIndex?.asLiveData()?.observe(binding?.lifecycleOwner ?: return) {
            if (it != 0) {
                (activity as? MainActivity)?.musicIndex = it
                // (activity as? MainActivity)?.playSong()
            } else {
                (activity as? MainActivity)?.stopSong()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}