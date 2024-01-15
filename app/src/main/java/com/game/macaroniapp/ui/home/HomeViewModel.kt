package com.game.macaroniapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.game.macaroniapp.preferences.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    var preferencesRepository: PreferencesRepository? = null
    var musicIndex: StateFlow<Int>? = null
}