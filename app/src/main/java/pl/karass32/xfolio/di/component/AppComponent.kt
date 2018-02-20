package pl.karass32.xfolio.di.component

import android.app.Application
import dagger.Component
import pl.karass32.xfolio.di.module.AppModule
import pl.karass32.xfolio.di.module.DatabaseModule
import pl.karass32.xfolio.di.module.NetworkModule
import pl.karass32.xfolio.ui.coinlist.CoinListViewModel
import javax.inject.Singleton

/**
 * Created by karas on 20.02.2018.
 */
@Singleton
@Component (modules = [AppModule::class, DatabaseModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(app: Application)
    fun inject(coinListViewModel: CoinListViewModel)
}