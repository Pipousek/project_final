package com.example.project_final
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project_final.api.RandomWordApiHandler
import com.example.project_final.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val sharedPreferencesFileName = "mySharedPreferences"
    private val stringSetKey = "myStringSet"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_game, R.id.nav_dictionary
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        lifecycleScope.launch {
            val randomWordApiHandler = RandomWordApiHandler()
            randomWordApiHandler.fetchRandomWord { response ->
                // Handle the API response here
                if (response != null) {
                    // Process the response string (which contains the random word)
                    var responseSet = response.joinToString(",")
                    if (sharedPreferences.contains(stringSetKey)) {
                        val oldData = sharedPreferences.getString(stringSetKey, "")
                        responseSet = "$oldData,$responseSet"
                    }
                    sharedPreferences.edit().putString(stringSetKey, responseSet).apply()
                    val receivedSharedPreferences = sharedPreferences.getString(stringSetKey, "")
                    println(receivedSharedPreferences)
                    // Update UI or perform other operations with the response string
                } else {
                    // Handle the case when response is null or API call fails
                    println("API call failed.")
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}