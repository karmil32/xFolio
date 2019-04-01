package pl.karass32.xfolio.ui.coinlist

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import kotlinx.android.synthetic.main.coin_global_data_layout.view.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import pl.karass32.xfolio.R
import pl.karass32.xfolio.base.BaseCoinListFragment
import pl.karass32.xfolio.data.GlobalCoinData
import pl.karass32.xfolio.util.CurrencyUtils

/**
 * Created by karas on 14.01.2018.
 */
class CoinListFragment : BaseCoinListFragment() {

    companion object {
        const val TAG = "CoinListFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(CoinListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.coin_list_fragment, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initToolbar() {
        super.initToolbar()
        mainActivity.supportActionBar?.title = getString(R.string.nav_all_coins)
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.getGlobalCoinData()?.observe(this, Observer { globalCoinData ->
            globalCoinData?.let { showGlobalCoinData(it, preferences.getDefaultCurrency()) }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showGlobalCoinData(globalData: GlobalCoinData, currencyCode: String) {
        val dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
        with(mView) {
            headerTotalMarketCap.text = CurrencyUtils.getFormattedBigValue(globalData.totalMarketCap, currencyCode)
            headerTotal24hVolume.text = CurrencyUtils.getFormattedBigValue(globalData.total24hVolume, currencyCode)
            headerBitcoinDominance.text = appContext.getString(R.string.percent_template, globalData.bitcoinDominance)
            headerActiveCurrencies.text = globalData.activeCurrencies.toString()
            headerActiveAssets.text = globalData.activeAssets.toString()
            headerActiveMarkets.text = globalData.activeMarkets.toString()
            headerLastUpdated.text = dateTimeFormat.print(DateTime(globalData.lastUpdated * 1000)).toString()
        }
    }
}