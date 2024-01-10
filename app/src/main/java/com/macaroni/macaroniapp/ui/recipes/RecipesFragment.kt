package com.macaroni.macaroniapp.ui.recipes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.macaroni.macaroniapp.CookingItemData
import com.macaroni.macaroniapp.R
import com.macaroni.macaroniapp.callback.MacaroniCallback
import com.macaroni.macaroniapp.databinding.CookingIntroFragmentBinding
import com.macaroni.macaroniapp.databinding.FragmentSlideshowBinding
import com.macaroni.macaroniapp.getRecipesData
import com.macaroni.macaroniapp.ui.cooking.intro.CookingIntroViewModel

class RecipesFragment : Fragment(), MacaroniCallback {

    private var binding: FragmentSlideshowBinding? = null
    private lateinit var viewModel: RecipesViewModel
    private var recipes: ArrayList<CookingItemData>? = null
    var cookingData: CookingItemData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)
        recipes = getRecipesData(resources)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_slideshow, container, false)
        binding?.lifecycleOwner = this.viewLifecycleOwner
        binding?.data = viewModel
        viewModel.callback = this

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.addPastaItems(getPastasIcons())
        viewModel.addSoupItems(getSoupsIcons())
        viewModel.addSaladItems(getSaladsIcons())
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