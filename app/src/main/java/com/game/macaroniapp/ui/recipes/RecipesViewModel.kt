package com.game.macaroniapp.ui.recipes

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

class RecipesViewModel : ViewModel() {

    val pastaAdapter = MacaroniAdapter()
    val soupsAdapter = MacaroniAdapter()
    val saladsAdapter = MacaroniAdapter()
    var preferencesRepository: PreferencesRepository? = null
    var numberOfCorrectFlow: StateFlow<String>? = null

    var callback: MacaroniCallback? = null

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    fun addPastaItems(listOfIcons: List<CookingItemData>, unlocked: ArrayList<Int>) {
        val call = callback ?: return
        listOfIcons.forEachIndexed { index, it ->
            val isPreviousUnlocked = unlocked.contains(it.levelTaps - 1)
            val locked = if (!isPreviousUnlocked && (it.isLocked && it.levelTaps != 3 && it.levelTaps != 14 && it.levelTaps != 16)) {
                View.VISIBLE
            } else {
                View.GONE
            }
            pastaAdapter.addItem(index, MacaroniItem(it, call, locked))
        }
    }

    fun addSoupItems(listOfIcons: List<CookingItemData>, unlocked: ArrayList<Int>) {
        val call = callback ?: return
        listOfIcons.forEachIndexed { index, it ->
            val isPreviousUnlocked = unlocked.contains(it.levelTaps - 1)
            val locked = if (!isPreviousUnlocked && (it.isLocked && it.levelTaps != 3 && it.levelTaps != 14 && it.levelTaps != 16)) {
                View.VISIBLE
            } else {
                View.GONE
            }
            soupsAdapter.addItem(index, MacaroniItem(it, call, locked))
        }
    }

    fun addSaladItems(listOfIcons: List<CookingItemData>, unlocked: ArrayList<Int>) {
        val call = callback ?: return
        listOfIcons.forEachIndexed { index, it ->
            val isPreviousUnlocked = unlocked.contains(it.levelTaps - 1)
            val locked = if (!isPreviousUnlocked && (it.isLocked && it.levelTaps != 3 && it.levelTaps != 14 && it.levelTaps != 16)) {
                View.VISIBLE
            } else {
                View.GONE
            }
            saladsAdapter.addItem(index, MacaroniItem(it, call, locked))
        }
    }
}