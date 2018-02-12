package pl.karass32.xfolio

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by karas on 12.02.2018.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}