package pl.karass32.xfolio.base

import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import pl.karass32.xfolio.MyApplication
import javax.inject.Inject

/**
 * Created by karas on 05.03.2018.
 */
abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var appContext: Application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.component.inject(this)
    }
}