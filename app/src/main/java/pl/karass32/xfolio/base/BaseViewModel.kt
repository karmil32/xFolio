package pl.karass32.xfolio.base

import android.arch.lifecycle.ViewModel
import pl.karass32.xfolio.MyApplication
import pl.karass32.xfolio.repository.api.CoinMarketCapService
import pl.karass32.xfolio.repository.api.FiatCurrencyService
import pl.karass32.xfolio.repository.db.AppDatabase
import pl.karass32.xfolio.repository.pref.SharedPreferencesRepository
import javax.inject.Inject

/**
 * Created by karas on 05.03.2018.
 */
abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var coinMarketCapService: CoinMarketCapService
    @Inject
    lateinit var fiatRatesService: FiatCurrencyService
    @Inject
    lateinit var appDb: AppDatabase
    @Inject
    lateinit var preferences: SharedPreferencesRepository

    init {
        MyApplication.component.inject(this)
    }
}