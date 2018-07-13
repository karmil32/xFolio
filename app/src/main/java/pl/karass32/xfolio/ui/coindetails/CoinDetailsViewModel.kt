package pl.karass32.xfolio.ui.coindetails

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import pl.karass32.xfolio.base.BaseViewModel
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.data.FiatCurrency
import pl.karass32.xfolio.data.HistDataResponse

class CoinDetailsViewModel : BaseViewModel() {
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    var coinDataMediator: MediatorLiveData<CoinData>? = null
    var histData: MutableLiveData<HistDataResponse>? = null
    var currency: MutableLiveData<FiatCurrency> = MutableLiveData()

    init {
        currency.value = appDb.fiatCurrencyDao().getCurrency(preferences.getDefaultCurrency()) // TODO MainThread
    }

    fun getCoinData(coinSymbol: String): LiveData<CoinData>? {
        if (coinDataMediator == null) {
            coinDataMediator = MediatorLiveData()
            coinDataMediator?.addSource(Transformations.switchMap(currency) { _ ->
                return@switchMap appDb.coinDataDao().getBySymbol(coinSymbol)
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

    fun getHistData(coinSymbol: String): LiveData<HistDataResponse>? {
        if (histData == null) {
            histData = MutableLiveData()
            loadHistData(coinSymbol)
        }
        return histData
    }

    fun loadHistData(coinSymbol: String) {
        compositeDisposable.add(cryptoCompareService.getAllHistoricalData(coinSymbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            histData?.value = result
                        },
                        { error ->
                        }
                ))
    }
}