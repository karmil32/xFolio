package pl.karass32.xfolio.di.component

import android.app.Application
import dagger.Component
import pl.karass32.xfolio.adapter.CoinRvAdapter
import pl.karass32.xfolio.base.BaseFragment
import pl.karass32.xfolio.base.BaseViewModel
import pl.karass32.xfolio.di.module.AppModule
import pl.karass32.xfolio.di.module.DatabaseModule
import pl.karass32.xfolio.di.module.NetworkModule
import pl.karass32.xfolio.di.module.PreferenceModule
import javax.inject.Singleton

/**
 * Created by karas on 20.02.2018.
 */
@Singleton
@Component (modules = [AppModule::class, DatabaseModule::class, NetworkModule::class, PreferenceModule::class])
interface AppComponent {
    fun inject(app: Application)
    fun inject(baseFragment: BaseFragment)
    fun inject(baseViewModel: BaseViewModel)
    fun inject(coinRvAdapter: CoinRvAdapter)
}