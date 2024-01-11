package com.macaroni.macaroniapp.ui.recipes

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.macaroni.macaroniapp.CookingItemData
import com.macaroni.macaroniapp.adapter.MacaroniAdapter
import com.macaroni.macaroniapp.adapter.MacaroniItem
import com.macaroni.macaroniapp.callback.MacaroniCallback
import com.macaroni.macaroniapp.preferences.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class RecipesViewModel : ViewModel() {

    val pastaAdapter = MacaroniAdapter()
    val soupsAdapter = MacaroniAdapter()
    val saladsAdapter = MacaroniAdapter()
    var preferencesRepository: PreferencesRepository? = null
    var numberOfCorrectFlow: StateFlow<Int>? = null

    var callback: MacaroniCallback? = null

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    fun addPastaItems(listOfIcons: List<CookingItemData> ) {
        val call = callback ?: return
        listOfIcons.forEachIndexed { index, it ->
            pastaAdapter.addItem(index, MacaroniItem(it, call, if (it.isLocked) {
                View.VISIBLE} else { View.GONE}))
        }
    }

    fun addSoupItems(listOfIcons: List<CookingItemData> ) {
        val call = callback ?: return
        listOfIcons.forEachIndexed { index, it ->
            soupsAdapter.addItem(index, MacaroniItem(it, call, if (it.isLocked) {
                View.VISIBLE} else { View.GONE}))
        }
    }

    fun addSaladItems(listOfIcons: List<CookingItemData> ) {
        val call = callback ?: return
        listOfIcons.forEachIndexed { index, it ->
            saladsAdapter.addItem(index, MacaroniItem(it, call, if (it.isLocked) {
                View.VISIBLE} else { View.GONE}))
        }
    }
}