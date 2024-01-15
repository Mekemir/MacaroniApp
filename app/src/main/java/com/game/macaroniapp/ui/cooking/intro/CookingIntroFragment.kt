package com.game.macaroniapp.ui.cooking.intro

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
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
import com.game.macaroniapp.databinding.CookingIntroFragmentBinding
import com.game.macaroniapp.getRecipesData
import com.game.macaroniapp.getUnlockedLevels
import com.game.macaroniapp.preferences.PreferencesRepository
import com.game.macaroniapp.ui.cooking.CookingFragment

class CookingIntroFragment: Fragment(), MacaroniCallback {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var binding: CookingIntroFragmentBinding? = null
    private lateinit var viewModel: CookingIntroViewModel
    private val categories = getCategories()
    private var recipes: ArrayList<CookingItemData>? = null
    var cookingData: CookingItemData? = null
    var allTimeCorrectNumber: String = "0?"
    var unlockedDishes: ArrayList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CookingIntroViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.cooking_intro_fragment, container,  false)
        binding?.lifecycleOwner = this.viewLifecycleOwner
        this.activity?.applicationContext?.let {
            viewModel.preferencesRepository = PreferencesRepository.getInstance(it)
            viewModel.numberOfCorrectFlow = viewModel.preferencesRepository?.numberOfCorrectFlow
        }
        viewModel.callback = this
        binding?.data  = viewModel

        binding?.homeBtn?.setOnClickListener {
            activity?.onBackPressed()
        }

//        // val textView: TextView = binding.textSlideshow
//        slideshowViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        binding?.getStartedBtn?.setOnClickListener {
            binding?.cookingIntro?.setBackgroundResource(R.drawable.cooking_back_further)
            binding?.header?.setBackgroundResource(R.drawable.choose_recipe_header)
            binding?.getStartedBtn?.visibility = View.GONE
            binding?.manIcon?.visibility = View.VISIBLE
            binding?.macaroniAdapter?.visibility = View.VISIBLE
            binding?.categoryHolder?.visibility = View.VISIBLE
        }

        binding?.arrowLeft?.setOnClickListener {
            val currentCategory = binding?.category?.text ?: "pasta"
            val currentIndex = categories.indexOf(currentCategory)
            if (currentIndex != 0) {
                handleLeft(currentIndex)
            }
        }
        binding?.arrowRight?.setOnClickListener {
            val currentCategory = binding?.category?.text ?: "pasta"
            val currentIndex = categories.indexOf(currentCategory)
            if (currentIndex != 2) {
                handleRight(currentIndex)
            }
        }
        binding?.startChoosenBtn?.setOnClickListener {
            cookingData ?: return@setOnClickListener
            val cookingFragment = CookingFragment()
            cookingFragment.cookingData = (cookingData)
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.drawer_layout, cookingFragment, "CookingFragment::class.java")
                ?.commit()
            binding?.choosenItemLayout?.visibility = View.GONE
        }
        binding?.choosenItemLayout?.setOnClickListener {
            binding?.choosenItemLayout?.visibility = View.GONE
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.numberOfCorrectFlow?.asLiveData()?.observe(binding?.lifecycleOwner ?: return) {
            if (it != "0?") {
                allTimeCorrectNumber = allTimeCorrectNumber + it
            }
            viewModel.adapter.clearItems()
            val getUnlocked = getUnlockedLevels(allTimeCorrectNumber)
            recipes = getRecipesData(resources, getUnlocked)
            viewModel.addItems(getPastasIcons(), getUnlocked)
        }
    }

    private fun handleRight(currentIndex: Int) {
        viewModel.adapter.clearItems()
        binding?.category?.text = categories.get(currentIndex + 1)
        if ((currentIndex + 1) == 1) {
            viewModel.addItems(getSoupsIcons(), getUnlockedLevels(allTimeCorrectNumber))
        }
        if ((currentIndex + 1) == 2) {
            viewModel.addItems(getSaladsIcons(), getUnlockedLevels(allTimeCorrectNumber))
        }
    }

    private fun handleLeft(currentIndex: Int) {
        viewModel.adapter.clearItems()
        binding?.category?.text = categories.get(currentIndex - 1)
        if ((currentIndex - 1) == 1) {
            viewModel.addItems(getSoupsIcons(), getUnlockedLevels(allTimeCorrectNumber))
        }
        if ((currentIndex - 1) == 0) {
            viewModel.addItems(getPastasIcons(), getUnlockedLevels(allTimeCorrectNumber))
        }
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

    private fun getCategories(): ArrayList<String> {
        val listOfCategories = arrayListOf<String>()
        listOfCategories.add("pasta")
        listOfCategories.add("soups")
        listOfCategories.add("salads")
        return listOfCategories
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCookingItemClicked(index: Int, cookingDrawableSelected: Drawable) {
        super.onCookingItemClicked(index, cookingDrawableSelected)
        binding?.choosenItemLayout?.visibility = View.VISIBLE
        binding?.choosenItemImage?.setImageDrawable(cookingDrawableSelected)
        cookingData = recipes?.find { it.levelTaps == index }

    }
}