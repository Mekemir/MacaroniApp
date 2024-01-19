package com.macaronisensation.ui.recipes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.macaronisensation.base.CookingItemData
import com.macaronisensation.R
import com.macaronisensation.callback.MacaroniCallback
import com.macaronisensation.base.getRecipesData
import com.macaronisensation.base.getUnlockedLevels
import com.macaronisensation.databinding.FragmentSlideshowBinding
import com.macaronisensation.preferences.PreferencesRepository

class RecipesFragment : Fragment(), MacaroniCallback {

    private var binding: FragmentSlideshowBinding? = null
    private lateinit var viewModel: RecipesViewModel
    private var recipes: ArrayList<CookingItemData>? = null
    var cookingData: CookingItemData? = null
    var allTimeCorrectNumber: String = "0?"
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
            if (it != "0?") {
                allTimeCorrectNumber = it
            }
            val getLocked = getUnlockedLevels(allTimeCorrectNumber)
            recipes = getRecipesData(resources, getLocked)
            viewModel.addPastaItems(getPastasIcons(), getLocked)
            viewModel.addSoupItems(getSoupsIcons(), getLocked)
            viewModel.addSaladItems(getSaladsIcons(), getLocked)
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