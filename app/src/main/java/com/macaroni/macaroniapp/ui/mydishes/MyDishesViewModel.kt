package com.macaroni.macaroniapp.ui.mydishes

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.macaroni.macaroniapp.CookingItemData
import com.macaroni.macaroniapp.adapter.MacaroniAdapter
import com.macaroni.macaroniapp.adapter.MacaroniItem
import com.macaroni.macaroniapp.adapter.MyDishesItem
import com.macaroni.macaroniapp.callback.MacaroniCallback
import com.macaroni.macaroniapp.preferences.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class MyDishesViewModel : ViewModel() {

    val pastaAdapter = MacaroniAdapter()
    var preferencesRepository: PreferencesRepository? = null
    var numberOfCorrectFlow: StateFlow<Int>? = null

    var callback: MacaroniCallback? = null

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    fun addDishesItems(listOfIcons: List<CookingItemData>? ) {
        val call = callback ?: return
        val listOfDishes = listOfIcons?.filter { !it.isLocked }
        if (listOfDishes?.size == 1) return
        listOfDishes?.forEachIndexed { index, it ->
            if (index != (listOfDishes.size - 1)) {
                pastaAdapter.addItem(index, MyDishesItem(it, call))
            }
        }
    }
}