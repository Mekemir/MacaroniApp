package com.game.macaroniapp.ui.recipes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.game.macaroniapp.CookingItemData
import com.game.macaroniapp.R
import com.game.macaroniapp.callback.MacaroniCallback
import com.game.macaroniapp.databinding.FragmentSlideshowBinding
import com.game.macaroniapp.getRecipesData
import com.game.macaroniapp.preferences.PreferencesRepository

class RecipesFragment : Fragment(), MacaroniCallback {

    private var binding: FragmentSlideshowBinding? = null
    private lateinit var viewModel: RecipesViewModel
    private var recipes: ArrayList<CookingItemData>? = null
    var cookingData: CookingItemData? = null
    var allTimeCorrectNumber: Int = 0
    var unlockedDishes: ArrayList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_slideshow, container, false)
        binding?.lifecycleOwner = this.viewLifecycleOwner
        this.activity?.applicationContext?.let {
            viewModel.preferencesRepository = PreferencesRepository.getInstance(it)
            viewModel.numberOfCorrectFlow = viewModel.preferencesRepository?.numberOfCorrectFlow
        }
        binding?.data = viewModel
        viewModel.callback = this

        binding?.homeBtn?.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.numberOfCorrectFlow?.asLiveData()?.observe(binding?.lifecycleOwner ?: return) {
            if (it != 0) {
                allTimeCorrectNumber = it
            }
            recipes = getRecipesData(resources, allTimeCorrectNumber)
            viewModel.addPastaItems(getPastasIcons())
            viewModel.addSoupItems(getSoupsIcons())
            viewModel.addSaladItems(getSaladsIcons())
        }
    }

        override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getPastasIcons(): List<CookingItemData> {

        return recipes?.take(11) ?: listOf()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getSoupsIcons(): List<CookingItemData> {

        return recipes?.subList(11, 13) ?: listOf()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getSaladsIcons(): List<CookingItemData> {

        return recipes?.subList(13, 16) ?: listOf()
    }
}