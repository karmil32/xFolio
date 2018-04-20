package pl.karass32.xfolio

import android.app.Application
import android.content.Intent
import net.danlew.android.joda.JodaTimeAndroid
import pl.karass32.xfolio.di.component.AppComponent
import pl.karass32.xfolio.di.component.DaggerAppComponent
import pl.karass32.xfolio.di.module.AppModule
import pl.karass32.xfolio.repository.pref.SharedPreferencesRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by karas on 12.02.2018.
 */
class MyApplication : Application() {

    @Inject
    lateinit var preferences: SharedPreferencesRepository

    companion object {
        lateinit var component: AppComponent
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
        component.inject(this)
        instance = this

        Timber.plant(Timber.DebugTree())
        JodaTimeAndroid.init(this)
    }

    private fun initDagger() {
        component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    fun restartApp() {
        val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        Runtime.getRuntime().exit(0)
    }
}