package com.macaronisensation.ui.music

import androidx.lifecycle.ViewModel
import com.macaronisensation.preferences.PreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class MusicViewModel: ViewModel() {

    var preferencesRepository: PreferencesRepository? = null
    var musicIndex: StateFlow<Int>? = null

    fun newSongChoosen(songIndex: Int) {
        preferencesRepository?.newAllAnswerAllTimeCount(songIndex)
    }
}