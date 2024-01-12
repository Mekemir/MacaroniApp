package com.game.macaroniapp.preferences

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val CORRECT_ANSWERS = "correct_answers"


class PreferencesRepository private constructor(context: Context) {

    private val sharedPreferences =
        context.applicationContext.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)

    private val _numberOfCorrectFlow = MutableStateFlow(correctNumber)
    val numberOfCorrectFlow: StateFlow<Int> = _numberOfCorrectFlow

    private val correctNumber: Int
        get() {
            return sharedPreferences.getInt(CORRECT_ANSWERS, 0) ?: 0
        }

    fun newCorrectAnswerAllTimeCount(allTimeCorrectNumber: Int) {

        updateSortOrder(allTimeCorrectNumber)
        _numberOfCorrectFlow.value = allTimeCorrectNumber
    }

    private fun updateSortOrder(numberOfCorrect: Int) {
        sharedPreferences.edit {
            putInt(CORRECT_ANSWERS, numberOfCorrect)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PreferencesRepository? = null

        fun getInstance(context: Context): PreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = PreferencesRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }
}