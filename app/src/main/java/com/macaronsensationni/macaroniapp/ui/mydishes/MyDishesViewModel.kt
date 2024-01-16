package com.macaronsensationni.macaroniapp.ui.mydishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.macaronsensationni.macaroniapp.CookingItemData
import com.macaronsensationni.macaroniapp.adapter.MacaroniAdapter
import com.macaronsensationni.macaroniapp.adapter.MyDishesItem
import com.macaronsensationni.macaroniapp.callback.MacaroniCallback
import com.macaronsensationni.macaroniapp.preferences.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class MyDishesViewModel : ViewModel() {

    val pastaAdapter = MacaroniAdapter()
    var preferencesRepository: PreferencesRepository? = null
    var numberOfCorrectFlow: StateFlow<String>? = null

    var callback: MacaroniCallback? = null

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    fun addDishesItems(listOfIcons: List<CookingItemData>? ) {
        val call = callback ?: return
        val listOfDishes = listOfIcons?.filter { !it.isLocked }
        listOfDishes?.forEachIndexed { index, it ->
            pastaAdapter.addItem(index, MyDishesItem(it, call))
        }
    }
}