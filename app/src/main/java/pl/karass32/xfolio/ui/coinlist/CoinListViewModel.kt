package pl.karass32.xfolio.ui.coinlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.data.GlobalCoinData
import pl.karass32.xfolio.error.CoinListErrorEvent
import pl.karass32.xfolio.extension.SingleLiveEvent
import pl.karass32.xfolio.repository.api.CoinMarketCapService
import pl.karass32.xfolio.util.CoinOrder
import kotlin.collections.ArrayList

/**
 * Created by karas on 01.02.2018.
 */
class CoinListViewModel : ViewModel() {

    private val coinMarketCapService by lazy {
        CoinMarketCapService.create()
    }

    private var globalDataDisposable: Disposable? = null
    private var globalCoinData: MutableLiveData<GlobalCoinData>? = null

    private var coinListDisposable: Disposable? = null
    private var coinList: MutableLiveData<ArrayList<CoinData>>? = null

    var coinListError = SingleLiveEvent<CoinListErrorEvent>()

    fun getGlobalCoinData() : LiveData<GlobalCoinData>? {
        if (globalCoinData == null) {
            globalCoinData = MutableLiveData()
            loadGlobalCoinData()
        }
        return globalCoinData
    }

    fun loadGlobalCoinData() {
        globalDataDisposable = coinMarketCapService.getGlobalCoinData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result ->
                            globalCoinData?.value = result
                        },
                        {_ ->

                        }
                )
    }

    fun getCoinList() : LiveData<ArrayList<CoinData>>? {
        if (coinList == null) {
            coinList = MutableLiveData()
            loadCoinList()
        }
        return coinList
    }

    fun loadCoinList() {
        coinListDisposable = coinMarketCapService.getCoinList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            coinList?.value = result
                        },
                        { _ ->
                            coinListError.value = CoinListErrorEvent.NO_SERVER_CONNECTION
                        }
                )
    }

    fun sortCoinList(sortMethod: CoinOrder) {
        val sortedList = when (sortMethod) {
            CoinOrder.BY_MARKET_CAP_DSC -> coinList?.value?.sortedBy { it.rank }
            CoinOrder.BY_MARKET_CAP_ASC -> coinList?.value?.sortedByDescending { it.rank }
            CoinOrder.BY_PRICE_DSC -> coinList?.value?.sortedByDescending { it.price }
            CoinOrder.BY_PRICE_ASC -> coinList?.value?.sortedBy { it.price }
            CoinOrder.BY_CHANGE_1H_DSC -> coinList?.value?.sortedByDescending { it.change1h }
            CoinOrder.BY_CHANGE_1H_ASC -> coinList?.value?.sortedBy { it.change1h }
            CoinOrder.BY_CHANGE_24H_DSC -> coinList?.value?.sortedByDescending { it.change24h }
            CoinOrder.BY_CHANGE_24H_ASC -> coinList?.value?.sortedBy { it.change24h }
        }

        coinList?.value = ArrayList(sortedList)
    }
}