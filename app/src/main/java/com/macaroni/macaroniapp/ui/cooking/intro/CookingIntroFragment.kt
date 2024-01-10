package com.macaroni.macaroniapp.ui.cooking.intro

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.macaroni.macaroniapp.CookingItemData
import com.macaroni.macaroniapp.R
import com.macaroni.macaroniapp.callback.MacaroniCallback
import com.macaroni.macaroniapp.databinding.CookingIntroFragmentBinding
import com.macaroni.macaroniapp.getRecipesData
import com.macaroni.macaroniapp.ui.cooking.CookingFragment

class CookingIntroFragment: Fragment(), MacaroniCallback {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var binding: CookingIntroFragmentBinding? = null
    private lateinit var viewModel: CookingIntroViewModel
    private val categories = getCategories()
    private var recipes: ArrayList<CookingItemData>? = null
    var cookingData: CookingItemData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CookingIntroViewModel::class.java)
        recipes = getRecipesData(resources)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.cooking_intro_fragment, container,  false)
        binding?.lifecycleOwner = this.viewLifecycleOwner
        viewModel.callback = this
        binding?.data  = viewModel


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
            cookingFragment.cookingData = cookingData
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.drawer_layout, cookingFragment, "CookingFragment::class.java")
                ?.commit()
        }
        binding?.choosenItemLayout?.setOnClickListener {
            binding?.choosenItemLayout?.visibility = View.GONE
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.addItems(getPastasIcons())
    }

    private fun handleRight(currentIndex: Int) {
        viewModel.adapter.clearItems()
        binding?.category?.text = categories.get(currentIndex + 1)
        if ((currentIndex + 1) == 1) {
            viewModel.addItems(getSoupsIcons())
        }
        if ((currentIndex + 1) == 2) {
            viewModel.addItems(getSaladsIcons())
        }
    }

    private fun handleLeft(currentIndex: Int) {
        viewModel.adapter.clearItems()
        binding?.category?.text = categories.get(currentIndex - 1)
        if ((currentIndex - 1) == 1) {
            viewModel.addItems(getSoupsIcons())
        }
        if ((currentIndex - 1) == 0) {
            viewModel.addItems(getPastasIcons())
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