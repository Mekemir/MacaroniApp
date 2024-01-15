package com.game.macaroniapp.ui.mydishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.game.macaroniapp.CookingItemData
import com.game.macaroniapp.adapter.MacaroniAdapter
import com.game.macaroniapp.adapter.MyDishesItem
import com.game.macaroniapp.callback.MacaroniCallback
import com.game.macaroniapp.preferences.PreferencesRepository
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