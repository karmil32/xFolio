package pl.karass32.xfolio.ui.coinlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.karass32.xfolio.MyApplication
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.data.GlobalCoinData
import pl.karass32.xfolio.error.CoinListErrorEvent
import pl.karass32.xfolio.extension.SingleLiveEvent
import pl.karass32.xfolio.repository.api.CoinMarketCapService
import pl.karass32.xfolio.repository.db.CoinDataDao
import pl.karass32.xfolio.repository.db.GlobalCoinDataDao
import pl.karass32.xfolio.util.CoinOrder
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by karas on 01.02.2018.
 */
class CoinListViewModel : ViewModel() {
    private var globalDataDisposable: Disposable? = null
    private var globalCoinData: MutableLiveData<GlobalCoinData>? = null

    private var coinListDisposable: Disposable? = null
    private var coinList: MutableLiveData<List<CoinData>>? = null

    var coinListError = SingleLiveEvent<CoinListErrorEvent>()

    @Inject lateinit var coinMarketCapService: CoinMarketCapService
    @Inject lateinit var coinDataDao: CoinDataDao
    @Inject lateinit var globalCoinDataDao: GlobalCoinDataDao

    init {
        MyApplication.component.inject(this)
    }

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
                            globalCoinDataDao.insertGlobalCoinData(result)
                        },
                        {_ ->
                            globalCoinData?.value = globalCoinDataDao.getGlobalCoinData()
                        }
                )
    }

    fun getCoinList() : LiveData<List<CoinData>>? {
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
                            coinDataDao.insertCoinData(result)
                        },
                        { _ ->
                            coinListError.value = CoinListErrorEvent.NO_SERVER_CONNECTION
                            coinList?.value = coinDataDao.getAllCoinData()
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