package com.macaronisensation.ui.mydishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.macaronisensation.base.CookingItemData
import com.macaronisensation.adapter.MacaroniAdapter
import com.macaronisensation.adapter.MyDishesItem
import com.macaronisensation.callback.MacaroniCallback
import com.macaronisensation.preferences.PreferencesRepository
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