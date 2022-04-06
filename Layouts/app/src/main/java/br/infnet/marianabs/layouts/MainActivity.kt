package br.infnet.marianabs.layouts

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.DARKMOOD
import br.infnet.marianabs.layouts.ui.user.utils.Constants.Companion.PREFERENCE
import br.infnet.marianabs.layouts.ui.user.utils.NotifyRepository
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotifyRepository().myNotification(this)
        this.preferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)

        preferredUserMode(preferences)
        setContentView(R.layout.activity_main)


        val navigation =
            supportFragmentManager.findFragmentById(R.id.container_fragment) as NavHostFragment
        val navController = navigation.navController

        val btn_nav = findViewById<BottomNavigationView>(R.id.bottomnavigation)
        btn_nav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, goTo, _ ->
            if (goTo.id == R.id.splash) {
                btn_nav.visibility = View.GONE
            }
            else if (goTo.id == R.id.loginFragment2) {
                btn_nav.visibility = View.GONE
            }
            else if (goTo.id == R.id.signUpFragment2) {
                btn_nav.visibility = View.GONE
            }
            else if (goTo.id == R.id.forgetPassword) {
                btn_nav.visibility = View.GONE
            }
            else {
                btn_nav.visibility = View.VISIBLE
            }
        }

    }

    fun preferredUserMode(preferences: SharedPreferences) {
        when {
            preferences.getBoolean(DARKMOOD, false) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


    }

}


