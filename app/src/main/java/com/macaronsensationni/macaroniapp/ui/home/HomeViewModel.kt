package com.macaronsensationni.macaroniapp.ui.home

import androidx.lifecycle.ViewModel
import com.macaronsensationni.macaroniapp.preferences.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    var preferencesRepository: PreferencesRepository? = null
    var musicIndex: StateFlow<Int>? = null
}