package pl.karass32.xfolio.base

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import pl.karass32.xfolio.MyApplication
import pl.karass32.xfolio.repository.pref.SharedPreferencesRepository
import pl.karass32.xfolio.util.ContextWrapper
import java.util.*
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var appContext: Application

    @Inject
    lateinit var preferences: SharedPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.component.inject(this)
        AppCompatDelegate.setDefaultNightMode(if (preferences.isNightModeEnabled()) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun attachBaseContext(base: Context) {
        val systemLang = Locale.getDefault().language
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(base)
        val langString = sharedPreferences.getString("general_app_language", "auto")
        val locale = Locale(if (langString == "auto") systemLang else langString)

        val context = ContextWrapper.wrap(base, locale)
        super.attachBaseContext(context)
    }
}