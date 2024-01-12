package com.game.macaroniapp.ui.cooking.intro

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.game.macaroniapp.CookingItemData
import com.game.macaroniapp.adapter.MacaroniAdapter
import com.game.macaroniapp.adapter.MacaroniItem
import com.game.macaroniapp.callback.MacaroniCallback
import com.game.macaroniapp.preferences.PreferencesRepository
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