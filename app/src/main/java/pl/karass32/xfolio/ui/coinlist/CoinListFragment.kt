package pl.karass32.xfolio.ui.coinlist

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.*
import kotlinx.android.synthetic.main.coin_global_data_layout.view.*
import kotlinx.android.synthetic.main.coin_list_fragment.view.*
import org.joda.time.DateTime
import pl.karass32.xfolio.R
import pl.karass32.xfolio.base.BaseCoinListFragment
import pl.karass32.xfolio.data.GlobalCoinData
import pl.karass32.xfolio.util.NumberUtils
import pl.karass32.xfolio.util.CurrencyUtils

/**
 * Created by karas on 14.01.2018.
 */
class CoinListFragment : BaseCoinListFragment() {

    companion object {
        const val TAG = "CoinListFragment"
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
        mViewModel.getCoinList()?.observe(this, Observer { coinList ->
            coinList?.let {
                showList(it)
                mView.rvError.visibility = View.GONE
            }
        })
        mViewModel.getGlobalCoinData()?.observe(this, Observer { globalCoinData ->
            globalCoinData?.let { showGlobalCoinData(it, preferences.getDefaultCurrency()) }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showGlobalCoinData(globalData: GlobalCoinData, currencyCode: String) {
        with(mView) {
            headerTotalMarketCap.text = CurrencyUtils.getFormattedBigValue(globalData.totalMarketCap, currencyCode)
            headerTotal24hVolume.text = CurrencyUtils.getFormattedBigValue(globalData.total24hVolume, currencyCode)
            headerBitcoinDominance.text = "${NumberUtils.percentageFormat.format(globalData.bitcoinDominance)}%"
            headerActiveCurrencies.text = globalData.activeCurrencies.toString()
            headerActiveAssets.text = globalData.activeAssets.toString()
            headerActiveMarkets.text = globalData.activeMarkets.toString()
            headerLastUpdated.text = NumberUtils.dateTimeFormat.print(DateTime(globalData.lastUpdated * 1000)).toString()
        }
    }
}