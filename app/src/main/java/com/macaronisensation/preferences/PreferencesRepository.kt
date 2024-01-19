package com.macaronisensation.preferences

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val CORRECT_ANSWERS = "correct_answers"
private const val ALL_TIME_NUMBER_OF_ANSWERS = "alltime"


class PreferencesRepository private constructor(context: Context) {

    private val sharedPreferences =
        context.applicationContext.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)

    private val _numberOfCorrectFlow = MutableStateFlow(correctNumber)
    val numberOfCorrectFlow: StateFlow<String> = _numberOfCorrectFlow

    private val _allTimeNumberOfAnswersCount = MutableStateFlow(allTimeNumberOfAnswers)
    val numberOfAllAnswersFlow: StateFlow<Int> = _allTimeNumberOfAnswersCount

    private val correctNumber: String
        get() {
            return sharedPreferences.getString(CORRECT_ANSWERS, "0?") ?: "0?"
        }

    fun newCorrectAnswerAllTimeCount(allTimeCorrectNumber: String) {

        updateSortOrder(allTimeCorrectNumber)
        _numberOfCorrectFlow.value = allTimeCorrectNumber
    }

    private fun updateSortOrder(numberOfCorrect: String) {
        sharedPreferences.edit {
            putString(CORRECT_ANSWERS, numberOfCorrect)
        }
    }

    private val allTimeNumberOfAnswers: Int
        get() {
            return sharedPreferences.getInt(ALL_TIME_NUMBER_OF_ANSWERS, 0)
        }

    fun newAllAnswerAllTimeCount(allTimeAnswersNumber: Int) {

        updateAllTimeAnswers(allTimeAnswersNumber)
        _allTimeNumberOfAnswersCount.value = allTimeAnswersNumber
    }

    private fun updateAllTimeAnswers(numberOfAnswers: Int) {
        sharedPreferences.edit {
            putInt(ALL_TIME_NUMBER_OF_ANSWERS, numberOfAnswers)
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