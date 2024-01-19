package com.macaronisensation.ui.cooking

import androidx.lifecycle.ViewModel
import com.macaronisensation.preferences.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class CookingViewModel : ViewModel() {

    var preferencesRepository: PreferencesRepository? = null
    var numberOfCorrectFlow: StateFlow<String>? = null

    fun newCorrectAnswerAllTimeCount(allTimeCorrectNumber: String) {
        preferencesRepository?.newCorrectAnswerAllTimeCount(allTimeCorrectNumber)
    }
}