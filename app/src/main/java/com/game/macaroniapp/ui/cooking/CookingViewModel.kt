package com.game.macaroniapp.ui.cooking

import androidx.lifecycle.ViewModel
import com.game.macaroniapp.preferences.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class CookingViewModel : ViewModel() {

    var preferencesRepository: PreferencesRepository? = null
    var numberOfCorrectFlow: StateFlow<Int>? = null

    fun newCorrectAnswerAllTimeCount(allTimeCorrectNumber: Int) {
        preferencesRepository?.newCorrectAnswerAllTimeCount(allTimeCorrectNumber)
    }
}