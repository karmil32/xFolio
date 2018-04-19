package pl.karass32.xfolio.base

import android.app.Application
import android.support.v7.app.AppCompatActivity
import pl.karass32.xfolio.repository.pref.SharedPreferencesRepository
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var appContext: Application

    @Inject
    lateinit var preferences: SharedPreferencesRepository
}