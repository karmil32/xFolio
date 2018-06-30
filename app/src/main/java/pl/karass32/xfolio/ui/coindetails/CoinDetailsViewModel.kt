package pl.karass32.xfolio.ui.coindetails

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import pl.karass32.xfolio.base.BaseViewModel
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.data.FiatCurrency

class CoinDetailsViewModel : BaseViewModel() {

    var coinDataMediator: MediatorLiveData<CoinData>? = null
    var currency: MutableLiveData<FiatCurrency> = MutableLiveData()

    init {
        currency.value = appDb.fiatCurrencyDao().getCurrency(preferences.getDefaultCurrency()) // TODO MainThread
    }

    fun getCoinData(id: String) : LiveData<CoinData>? {
        if (coinDataMediator == null) {
            coinDataMediator = MediatorLiveData()
            coinDataMediator?.addSource(Transformations.switchMap(currency) { _ ->
                return@switchMap appDb.coinDataDao().getById(id)
            }) { coinData ->
                if (preferences.getDefaultCurrency() != "USD") {
                    coinData?.let {
                        it.price = it.price?.multiply(currency.value?.rate)
                        it.volume24h = it.volume24h?.multiply(currency.value?.rate)
                        it.marketCap = it.marketCap?.multiply(currency.value?.rate)
                    }
                }
                coinDataMediator?.value = coinData
            }
        }
        return coinDataMediator
    }
}