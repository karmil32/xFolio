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
import pl.karass32.xfolio.util.CoinOrder
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


/**
 * Created by karas on 01.02.2018.
 */
class CoinListViewModel : BaseViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var globalCoinDataMediator: MediatorLiveData<GlobalCoinData>? = null
    private var coinListMediator: MediatorLiveData<List<CoinData>>? = null
    private var fiatCodesMediator: MediatorLiveData<Array<String>>? = null
    private var currency: MutableLiveData<FiatCurrency> = MutableLiveData()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var coinListError = SingleLiveEvent<CoinListErrorEvent>()

    init {
        currency.value = appDb.fiatCurrencyDao().getCurrency(preferences.getDefaultCurrency()) // TODO MainThread
    }

    fun getGlobalCoinData(): LiveData<GlobalCoinData>? {
        if (globalCoinDataMediator == null) {
            globalCoinDataMediator = MediatorLiveData()
            globalCoinDataMediator?.addSource(appDb.globalCoinDataDao().getGlobalCoinData(), { globalData ->
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
                        coin.price = coin.price?.multiply(currency.value?.currencyRate)
                        coin.volume24h = coin.volume24h?.multiply(currency.value?.currencyRate)
                        coin.marketCap = coin.marketCap?.multiply(currency.value?.currencyRate)
                    }
                }
                coinListMediator?.value = list
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

    fun getFiatStringCodes(): LiveData<Array<String>>? {
        if (fiatCodesMediator == null) {
            fiatCodesMediator = MediatorLiveData()
            fiatCodesMediator?.addSource(appDb.fiatCurrencyDao().getAllFiatCodes(), { codes ->
                fiatCodesMediator?.value = codes
            })
            loadFiatRates()
        }
        return fiatCodesMediator
    }

    fun loadFiatRates() {
        compositeDisposable.add(fiatRatesService.getLatestRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            thread { appDb.fiatCurrencyDao().updateRates(result) }
                        },
                        { _ -> }
                ))
    }

    fun setCurrency(fiatCode: String) {
        preferences.setDefaultCurrency(fiatCode)
        thread { currency.postValue(appDb.fiatCurrencyDao().getCurrency(fiatCode)) }
    }

    fun sortCoinList(sortMethod: CoinOrder) {
        val sortedList = when (sortMethod) {
            CoinOrder.BY_MARKET_CAP_DSC -> coinListMediator?.value?.sortedBy { it.rank }
            CoinOrder.BY_MARKET_CAP_ASC -> coinListMediator?.value?.sortedByDescending { it.rank }
            CoinOrder.BY_PRICE_DSC -> coinListMediator?.value?.sortedByDescending { it.price }
            CoinOrder.BY_PRICE_ASC -> coinListMediator?.value?.sortedBy { it.price }
            CoinOrder.BY_CHANGE_1H_DSC -> coinListMediator?.value?.sortedByDescending { it.change1h }
            CoinOrder.BY_CHANGE_1H_ASC -> coinListMediator?.value?.sortedBy { it.change1h }
            CoinOrder.BY_CHANGE_24H_DSC -> coinListMediator?.value?.sortedByDescending { it.change24h }
            CoinOrder.BY_CHANGE_24H_ASC -> coinListMediator?.value?.sortedBy { it.change24h }
        }

        coinListMediator?.value = ArrayList(sortedList)
    }
}