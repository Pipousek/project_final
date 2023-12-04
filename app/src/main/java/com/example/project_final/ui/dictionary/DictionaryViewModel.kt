package com.example.project_final.ui.dictionary
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.project_final.api.RandomWordApiHandler

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferencesFileName = "mySharedPreferences"
    private val stringSetKey = "myStringSet"
    private val randomWordApiHandler = RandomWordApiHandler()

    internal fun updateSharePrefenerces() {
        randomWordApiHandler.fetchRandomWord { response ->
            if (response != null) {
                val sharedPreferences = getApplication<Application>().getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
                var responseSet = response.joinToString(",")
                if (sharedPreferences.contains(stringSetKey)) {
                    val oldData = sharedPreferences.getString(stringSetKey, "")
                    responseSet = "$oldData,$responseSet"
                }
                sharedPreferences.edit().putString(stringSetKey, responseSet).apply()
                println(response)
            }
        }
    }

    internal fun returnWordsAsList(): List<String> {
        val sharedPreferences = getApplication<Application>().getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
        val tmp = sharedPreferences.getString(stringSetKey, "")
        return tmp!!.split(",")
    }
}