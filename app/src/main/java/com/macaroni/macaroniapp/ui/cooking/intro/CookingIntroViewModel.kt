package com.macaroni.macaroniapp.ui.cooking.intro

import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macaroni.macaroniapp.CookingItemData
import com.macaroni.macaroniapp.R
import com.macaroni.macaroniapp.adapter.MacaroniAdapter
import com.macaroni.macaroniapp.adapter.MacaroniItem
import com.macaroni.macaroniapp.callback.MacaroniCallback
import com.macaroni.macaroniapp.preferences.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class CookingIntroViewModel : ViewModel() {

    val adapter = MacaroniAdapter()
    var callback: MacaroniCallback? = null
    var preferencesRepository: PreferencesRepository? = null
    var numberOfCorrectFlow: StateFlow<Int>? = null

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    fun addItems(listOfIcons: List<CookingItemData> ) {
        val call = callback ?: return
        listOfIcons.forEachIndexed { index, it ->
            adapter.addItem(index, MacaroniItem(it, call, if (it.isLocked) {
                View.VISIBLE} else { View.GONE}))
        }
    }
}