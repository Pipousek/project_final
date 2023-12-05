package com.example.project_final.ui.game

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.project_final.getSharedPreferencesFileName
import com.example.project_final.getStringSetKey

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferencesFileName = getSharedPreferencesFileName()
    private val stringSetKey = getStringSetKey()

    internal fun getRandomWordFromDictionary(): String {
        val sharedPreferences = getApplication<Application>().getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
        val storedWords = sharedPreferences.getString(stringSetKey, "")?.split(",")
        val randNum = (0..storedWords!!.count() - 1).random()
        return storedWords[randNum]
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
        return result
    }

}