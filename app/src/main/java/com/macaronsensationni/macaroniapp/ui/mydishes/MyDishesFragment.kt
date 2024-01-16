package com.macaronsensationni.macaroniapp.ui.mydishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.macaronsensationni.macaroniapp.CookingItemData
import com.macaronsensationni.macaroniapp.R
import com.macaronsensationni.macaroniapp.callback.MacaroniCallback
import com.macaronsensationni.macaroniapp.databinding.MyDishesFragmentBinding
import com.macaronsensationni.macaroniapp.getRecipesData
import com.macaronsensationni.macaroniapp.getUnlockedLevels
import com.macaronsensationni.macaroniapp.preferences.PreferencesRepository

class MyDishesFragment : Fragment(), MacaroniCallback {

    private var binding: MyDishesFragmentBinding? = null
    private lateinit var viewModel: MyDishesViewModel
    private var recipes: ArrayList<CookingItemData>? = null
    var cookingData: CookingItemData? = null
    var allTimeCorrectNumber: String = "0?"
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
            if (it != "0?") {
                allTimeCorrectNumber =  allTimeCorrectNumber + it.toString()
            }
            recipes = getRecipesData(resources, getUnlockedLevels(allTimeCorrectNumber))
            viewModel.addDishesItems(recipes?.filter { !it.isLocked })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}