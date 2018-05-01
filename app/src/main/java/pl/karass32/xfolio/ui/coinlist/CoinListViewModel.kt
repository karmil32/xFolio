package pl.karass32.xfolio.ui.coinlist

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
import pl.karass32.xfolio.data.GlobalCoinData
import pl.karass32.xfolio.error.CoinListErrorEvent
import pl.karass32.xfolio.extension.SingleLiveEvent
import pl.karass32.xfolio.util.CoinListUtils
import pl.karass32.xfolio.util.CoinOrder
import java.math.BigDecimal
import kotlin.concurrent.thread

/**
 * Created by karas on 01.02.2018.
 */
class CoinListViewModel : BaseViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var globalCoinDataMediator: MediatorLiveData<GlobalCoinData>? = null
    private var coinListMediator: MediatorLiveData<List<CoinData>>? = null
    private var currency: MutableLiveData<FiatCurrency> = MutableLiveData()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var coinListError = SingleLiveEvent<CoinListErrorEvent>()

    init {
        currency.value = appDb.fiatCurrencyDao().getCurrency(preferences.getDefaultCurrency()) // TODO MainThread
        loadFiatRates() // TODO Temp
    }

    fun getGlobalCoinData(): LiveData<GlobalCoinData>? {
        if (globalCoinDataMediator == null) {
            globalCoinDataMediator = MediatorLiveData()
            globalCoinDataMediator?.addSource(Transformations.switchMap(currency, { _ ->
                return@switchMap appDb.globalCoinDataDao().getGlobalCoinData()
            }), { globalData ->
                if (preferences.getDefaultCurrency() != "USD") {
                    globalData?.let {
                        it.totalMarketCap = BigDecimal(it.totalMarketCap).multiply(currency.value?.rate).toLong()
                        it.total24hVolume = BigDecimal(it.total24hVolume).multiply(currency.value?.rate).toLong()
                    }
                }
                globalCoinDataMediator?.value = globalData
            })

            loadGlobalCoinData()
        }
        return globalCoinDataMediator
    }

    fun loadGlobalCoinData() {
        compositeDisposable.add(coinMarketCapService.getGlobalCoinData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> thread { appDb.globalCoinDataDao().insertGlobalCoinData(result) } },
                        { _ -> }
                ))
    }

    fun getCoinList(): LiveData<List<CoinData>>? {
        if (coinListMediator == null) {
            coinListMediator = MediatorLiveData()
            coinListMediator?.addSource(Transformations.switchMap(currency, { _ ->
                return@switchMap appDb.coinDataDao().getAllCoinData()
            }), { list ->
                if (preferences.getDefaultCurrency() != "USD") {
                    list?.forEach { coin ->
                        coin.price = coin.price?.multiply(currency.value?.rate)
                        coin.volume24h = coin.volume24h?.multiply(currency.value?.rate)
                        coin.marketCap = coin.marketCap?.multiply(currency.value?.rate)
                    }
                }
                if (preferences.getCoinListOrder() != CoinOrder.BY_MARKET_CAP_DSC.value) {
                    coinListMediator?.value = CoinListUtils.sort(list, CoinOrder.of(preferences.getCoinListOrder()))
                } else {
                    coinListMediator?.value = list
                }
            })
            loadCoinList()
        }
        return coinListMediator
    }

    fun loadCoinList() {
        isLoading.value = true
        compositeDisposable.add(coinMarketCapService.getCoinList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            thread { appDb.coinDataDao().insertCoinData(result) }
                            isLoading.value = false
                        },
                        { _ ->
                            coinListError.value = CoinListErrorEvent.NO_SERVER_CONNECTION
                            isLoading.value = false
                        }
                ))
    }

    fun loadFiatRates() {
        compositeDisposable.add(currencyRatesService.getLatestRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            val list = result.rates.map { FiatCurrency(it.key.drop(3), it.value, result.timestamp) }.toList()
                            thread { appDb.fiatCurrencyDao().updateRates(list) }
                        },
                        { _ -> }
                ))
    }

    fun setCurrency(fiatCode: String) {
        preferences.setDefaultCurrency(fiatCode)
        thread { currency.postValue(appDb.fiatCurrencyDao().getCurrency(fiatCode)) }
    }

    fun changeListOrder(sortMethod: CoinOrder) {
        preferences.setCoinListOrder(sortMethod.value)
        val sortedList = CoinListUtils.sort(coinListMediator?.value, sortMethod)
        coinListMediator?.value = sortedList
    }
}