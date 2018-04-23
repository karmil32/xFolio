package pl.karass32.xfolio.base

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import pl.karass32.xfolio.repository.pref.SharedPreferencesRepository
import pl.karass32.xfolio.util.ContextWrapper
import timber.log.Timber
import java.util.*
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var appContext: Application

    @Inject
    lateinit var preferences: SharedPreferencesRepository

    override fun attachBaseContext(base: Context) {
        val systemLang = Locale.getDefault().language
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(base)
        val langString = sharedPreferences.getString("general_app_language", "auto")
        val locale = Locale(if (langString == "auto") systemLang else langString)

        val context = ContextWrapper.wrap(base, locale)
        super.attachBaseContext(context)
    }
}