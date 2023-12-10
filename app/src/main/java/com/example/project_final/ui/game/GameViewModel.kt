package com.example.project_final.ui.game

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.project_final.*

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferencesFileName = getSharedPreferencesFileName()
    private val stringSetKey = getStringSetKey()
    private val origWordKey = getOrigWordKey()
    private val currentGameWord = getCurrentGameWordFromStorage()
    private val currentGameAttempts = getCurrentGameAttemptsFromStorage()
    private val currentGameWordStatus = getCurrentGameWordStatus()
    private val sharedPreferences = getApplication<Application>().getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)

    internal fun getRandomWordFromDictionary(): String {
        val storedWords = sharedPreferences.getString(stringSetKey, "")?.split(",")
        val randNum = (0..storedWords!!.count() - 1).random()
        val randomWord = storedWords[randNum]
        sharedPreferences.edit().putString(currentGameWord, randomWord).apply()
        sharedPreferences.edit().putInt(currentGameAttempts, 0).apply()
        sharedPreferences.edit().putInt(currentGameWordStatus, 0).apply()
        return randomWord
    }

    internal fun checkWord(origWord: String, guessWord: String): List<Int> {
        val result = mutableListOf<Int>()
        for (idx in (0..origWord.length - 1)) {
            if (guessWord[idx] in origWord) {
                if (guessWord[idx] == origWord[idx]) {
                    result.add(idx, 2)
                } else {
                    result.add(idx, 1)
                }
            } else {
                result.add(idx, 0)
            }
        }
        sharedPreferences.edit().putString(origWordKey, result.joinToString(",")).apply()
        return result
    }

    internal fun getCurrentGameWord(): String? {
        return sharedPreferences.getString(currentGameWord, "")
    }

    internal fun getCurrentGameAttempts(): Int {
        return sharedPreferences.getInt(currentGameAttempts, 0)
    }

    internal fun setCurrentGameAttempts(attemptCount: Int) {
        sharedPreferences.edit().putInt(currentGameAttempts, attemptCount).apply()
    }

    internal fun getCurrentWordStatus(): Int {
        return sharedPreferences.getInt(currentGameWordStatus, 0)
    }

    internal fun setCurrentWordStatus(gameWordStatus: Int) {
        sharedPreferences.edit().putInt(currentGameWordStatus, gameWordStatus).apply()
    }
}