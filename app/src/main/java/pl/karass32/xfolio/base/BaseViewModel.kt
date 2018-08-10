package pl.karass32.xfolio.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import pl.karass32.xfolio.MyApplication
import pl.karass32.xfolio.data.FiatCurrency
import pl.karass32.xfolio.repository.api.CoinMarketCapService
import pl.karass32.xfolio.repository.api.CryptoCompareService
import pl.karass32.xfolio.repository.db.AppDatabase
import pl.karass32.xfolio.repository.api.CurrencyRatesService
import pl.karass32.xfolio.repository.pref.SharedPreferencesRepository
import javax.inject.Inject

/**
 * Created by karas on 05.03.2018.
 */
abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var coinMarketCapService: CoinMarketCapService
    @Inject
    lateinit var currencyRatesService: CurrencyRatesService
    @Inject
    lateinit var cryptoCompareService: CryptoCompareService
    @Inject
    lateinit var appDb: AppDatabase
    @Inject
    lateinit var preferences: SharedPreferencesRepository

    var currency: MutableLiveData<FiatCurrency> = MutableLiveData()

    init {
        MyApplication.component.inject(this)
    }
}