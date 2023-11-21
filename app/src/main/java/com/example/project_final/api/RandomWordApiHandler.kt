package com.example.project_final.api
import okhttp3.*
import java.io.IOException

class RandomWordApiHandler {

    fun fetchRandomWord(callback: (List<String>?) -> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://random-word-api.herokuapp.com/word?number=10&length=5")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure here
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()

                    val words = responseBody?.replace("[", "")?.replace("]", "")?.replace("\"", "")?.split(",")

                    // Pass the response string through the callback
                    callback(words)
                } else {
                    // Handle unsuccessful response
                    callback(null)
                }
            }
        })
    }
}