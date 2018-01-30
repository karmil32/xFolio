package pl.karass32.xfolio.ui.coinlist

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.karass32.xfolio.repository.api.CoinMarketCapService

/**
 * Created by karas on 16.01.2018.
 */
class CoinListPresenter(private val view: CoinListContract.View) : CoinListContract.Presenter {

    private val coinMarketCapService by lazy {
        CoinMarketCapService.create()
    }

    private var disposable: Disposable? = null

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCoinList() {
        Log.d("getCoinList", "start")
        view.showRefreshSpinner()

        disposable = coinMarketCapService.getCoinList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            view.showList(result)
                            view.hideRefreshSpinner()
                        },
                        { error ->
                            view.showError(error.toString())
                            view.hideRefreshSpinner()
                        }
                )
    }

    override fun onDetach() {
        disposable?.dispose()
    }
}