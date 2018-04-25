package pl.karass32.xfolio.di.module

import dagger.Module
import dagger.Provides
import pl.karass32.xfolio.repository.api.CoinMarketCapService
import pl.karass32.xfolio.repository.api.CurrencyRatesService
import javax.inject.Singleton

/**
 * Created by karas on 21.02.2018.
 */
@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideCoinMarketCapAPI() = CoinMarketCapService.create()

    @Provides
    @Singleton
    fun provideCurrencyRatesAPI() = CurrencyRatesService.create()
}