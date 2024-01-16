package com.macaronsensationni.macaroniapp.ui.cooking.intro

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.macaronsensationni.macaroniapp.CookingItemData
import com.macaronsensationni.macaroniapp.adapter.MacaroniAdapter
import com.macaronsensationni.macaroniapp.adapter.MacaroniItem
import com.macaronsensationni.macaroniapp.callback.MacaroniCallback
import com.macaronsensationni.macaroniapp.preferences.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class CookingIntroViewModel : ViewModel() {

    val adapter = MacaroniAdapter()
    var callback: MacaroniCallback? = null
    var preferencesRepository: PreferencesRepository? = null
    var numberOfCorrectFlow: StateFlow<String>? = null

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    fun addItems(listOfIcons: List<CookingItemData>, unlocked: ArrayList<Int>) {
        val call = callback ?: return
        listOfIcons.forEachIndexed { index, it ->
            val isPreviousUnlocked = unlocked.contains(it.levelTaps - 1)
            val locked = if (!isPreviousUnlocked && (it.isLocked && it.levelTaps != 3 && it.levelTaps != 14 && it.levelTaps != 16)) {
                View.VISIBLE
            } else {
                View.GONE
            }
            adapter.addItem(index, MacaroniItem(it, call, locked))
        }
    }
}