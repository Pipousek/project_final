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
import com.example.project_final.databinding.ActivityMainBinding

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

        sharedPreferences.edit().remove(stringSetKey).apply()
        if (!sharedPreferences.contains(stringSetKey)) {
            sharedPreferences.edit().putString(stringSetKey, defaultWordsData()).apply()
        }
        val receivedSharedPreferences = sharedPreferences.getString(stringSetKey, "")
        println(receivedSharedPreferences)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun defaultWordsData(): String {
        return listOf(
            "amber", "blame", "chase", "diver", "eagle",
            "fable", "grasp", "haste", "inset", "joust",
            "knack", "latch", "mirth", "nudge", "olive",
            "pouch", "quirk", "rally", "scent", "trace",
            "umbra", "viper", "widen", "xerox", "yacht",
            "zebra", "frown", "glide", "humor", "icily",
            "jumpy", "knead", "latch", "maple", "navel",
            "overt", "plush", "quest", "rigid", "snack",
            "tramp", "untie", "virus", "wrist", "xylon",
            "yodel", "zesty", "blaze", "cower", "dusky"
        ).joinToString(",")
    }
}