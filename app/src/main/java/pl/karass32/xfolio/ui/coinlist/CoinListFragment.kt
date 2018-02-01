package pl.karass32.xfolio.ui.coinlist

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.coin_global_data_layout.view.*
import kotlinx.android.synthetic.main.coin_list_fragment.view.*
import pl.karass32.xfolio.MainActivity
import pl.karass32.xfolio.R
import pl.karass32.xfolio.adapter.CoinRvAdapter
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.data.GlobalCoinData
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Created by karas on 14.01.2018.
 */
class CoinListFragment : Fragment(), CoinListContract.View {

    private lateinit var mainActivity: MainActivity
    private lateinit var mPresenter : CoinListContract.Presenter
    private lateinit var mView : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.coin_list_fragment, container, false)

        mainActivity = activity as MainActivity
        mainActivity.setSupportActionBar(mView.toolbar)
        mainActivity.supportActionBar?.setDisplayShowHomeEnabled(true)
        mainActivity.supportActionBar?.title = getString(R.string.nav_all_coins)
        mainActivity.setToggle(mView.toolbar)

        setPresenter(CoinListPresenter(this))
        initSwipeRefreshLayout()

        mPresenter.getGlobalCoinData()
        mPresenter.getCoinList()

        return mView
    }

    override fun setPresenter(presenter: CoinListContract.Presenter) {
        mPresenter = presenter
    }

    override fun initSwipeRefreshLayout() {
        mView.coinListSwipeRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE)
        mView.coinListSwipeRefresh.setOnRefreshListener {
            mPresenter.getCoinList()
        }
    }

    override fun showRefreshSpinner() {
        mView.coinListSwipeRefresh.isRefreshing = true
    }

    override fun hideRefreshSpinner() {
        mView.coinListSwipeRefresh.isRefreshing = false
    }

    @SuppressLint("SetTextI18n")
    override fun showGlobalCoinData(globalCoinData: GlobalCoinData) {
        Log.d("showGlobalData", "start")

        val bigValuePattern = "###,###.##"
        val bigValueFormat = DecimalFormat(bigValuePattern)

        val percentagePattern = "#0.00"
        val percentageFormat = DecimalFormat(percentagePattern)

        mView.headerTotalMarketCapValue.text = "$${bigValueFormat.format(globalCoinData.totalMarketCap)}"
        mView.headerTotal24hVolumeValue.text = "$${bigValueFormat.format(globalCoinData.total24hVolume)}"
        mView.headerBitcoinDominanceValue.text = "${percentageFormat.format(globalCoinData.bitcoinDominance)}%"
        mView.headerActiveCurrenciesValue.text = globalCoinData.activeCurrencies.toString()
        mView.headerActiveAssetsValue.text = globalCoinData.activeAssets.toString()
        mView.headerActiveMarketsValue.text = globalCoinData.activeMarkets.toString()
    }

    override fun showList(list: ArrayList<CoinData>) {
        Log.d("showList", "start")
        mView.coinListRv?.setHasFixedSize(true)
        mView.coinListRv?.layoutManager = LinearLayoutManager(mainActivity)
        mView.coinListRv?.adapter = CoinRvAdapter(list)
    }

    override fun showError(error: String) {
        Log.d("showError", error)
        Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show()
    }
}