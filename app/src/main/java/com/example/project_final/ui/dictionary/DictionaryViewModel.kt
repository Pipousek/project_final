package com.example.project_final.ui.dictionary
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.project_final.api.RandomWordApiHandler
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferencesFileName = "mySharedPreferences"
    private val stringSetKey = "myStringSet"
    private val randomWordApiHandler = RandomWordApiHandler()

    internal suspend fun updateSharePrefenerces(): List<String> {
        return suspendCoroutine { continuation ->
            randomWordApiHandler.fetchRandomWord { response ->
                if (response != null) {
                    val sharedPreferences = getApplication<Application>().getSharedPreferences(
                        sharedPreferencesFileName,
                        Context.MODE_PRIVATE
                    )
                    val oldData = sharedPreferences.getString(stringSetKey, "")?.split(",")
                    var responseList = response.toMutableList()
                    println(responseList.count())

                    if (oldData != null) {
                        for (word in response) {
                            if (word in oldData) {
                                println(word)
                                responseList.remove(word)
                            }
                        }
                    }

                    var responseSet = responseList.joinToString(",")
                    if (sharedPreferences.contains(stringSetKey)) {
                        responseSet = "${oldData?.joinToString(",")},$responseSet"
                    }
                    sharedPreferences.edit().putString(stringSetKey, responseSet).apply()

                    println(responseList.count())
                    println(response)
                    println(responseList)
                    println(responseSet)

                    // Return the updated list
                    val updatedList = responseSet.split(",")
                    continuation.resume(updatedList)
                }
            }
        }
    }

    internal fun returnWordsAsList(): List<String> {
        val sharedPreferences = getApplication<Application>().getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
        val tmp = sharedPreferences.getString(stringSetKey, "")
        return tmp!!.split(",")
    }
}