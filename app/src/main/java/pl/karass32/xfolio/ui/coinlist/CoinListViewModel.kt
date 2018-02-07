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
}