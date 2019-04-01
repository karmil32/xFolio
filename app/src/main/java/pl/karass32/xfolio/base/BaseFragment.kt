package pl.karass32.xfolio.base

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import pl.karass32.xfolio.MyApplication
import pl.karass32.xfolio.repository.pref.SharedPreferencesRepository
import javax.inject.Inject

/**
 * Created by karas on 05.03.2018.
 */
abstract class BaseFragment : androidx.fragment.app.Fragment() {

    @Inject
    lateinit var appContext: Application

    @Inject
    lateinit var preferences: SharedPreferencesRepository

    lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.component.inject(this)
    }
}