package pl.karass32.xfolio.ui.coinlist

import pl.karass32.xfolio.base.BasePresenter
import pl.karass32.xfolio.base.BaseView
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.data.GlobalCoinData

/**
 * Created by karas on 17.01.2018.
 */
interface CoinListContract {

    interface View : BaseView<Presenter> {
        fun showGlobalCoinData(globalCoinData: GlobalCoinData)
        fun showList(list: ArrayList<CoinData>)
        fun initSwipeRefreshLayout()
        fun showRefreshSpinner()
        fun hideRefreshSpinner()
        fun showError(error: String)
    }

    interface Presenter : BasePresenter {
        fun getGlobalCoinData()
        fun getCoinList()
        fun onDetach()
    }
}