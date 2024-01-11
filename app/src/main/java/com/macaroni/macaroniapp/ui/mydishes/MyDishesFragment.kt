package com.macaroni.macaroniapp.ui.mydishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.macaroni.macaroniapp.CookingItemData
import com.macaroni.macaroniapp.R
import com.macaroni.macaroniapp.callback.MacaroniCallback
import com.macaroni.macaroniapp.databinding.FragmentSlideshowBinding
import com.macaroni.macaroniapp.databinding.MyDishesFragmentBinding
import com.macaroni.macaroniapp.getRecipesData
import com.macaroni.macaroniapp.preferences.PreferencesRepository
import com.macaroni.macaroniapp.ui.recipes.RecipesViewModel

class MyDishesFragment : Fragment(), MacaroniCallback {

    private var binding: MyDishesFragmentBinding? = null
    private lateinit var viewModel: MyDishesViewModel
    private var recipes: ArrayList<CookingItemData>? = null
    var cookingData: CookingItemData? = null
    var allTimeCorrectNumber: Int = 0
    var unlockedDishes: ArrayList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyDishesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.my_dishes_fragment, container, false)
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
            viewModel.addDishesItems(recipes?.filter { !it.isLocked })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}