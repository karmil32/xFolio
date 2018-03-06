package pl.karass32.xfolio.base

import android.arch.lifecycle.ViewModel
import pl.karass32.xfolio.MyApplication
import pl.karass32.xfolio.repository.api.CoinMarketCapService
import pl.karass32.xfolio.repository.api.FiatCurrencyService
import pl.karass32.xfolio.repository.db.CoinDataDao
import pl.karass32.xfolio.repository.db.FiatCurrencyDao
import pl.karass32.xfolio.repository.db.GlobalCoinDataDao
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
    lateinit var coinDataDao: CoinDataDao
    @Inject
    lateinit var globalCoinDataDao: GlobalCoinDataDao
    @Inject
    lateinit var fiatCurrencyDao: FiatCurrencyDao

    init {
        MyApplication.component.inject(this)
    }
}