package pl.karass32.xfolio

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid
import pl.karass32.xfolio.di.component.AppComponent
import pl.karass32.xfolio.di.component.DaggerAppComponent
import pl.karass32.xfolio.di.module.AppModule

/**
 * Created by karas on 12.02.2018.
 */
class MyApplication : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)

        initDagger()
        component.inject(this)
    }

    private fun initDagger() {
        component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}