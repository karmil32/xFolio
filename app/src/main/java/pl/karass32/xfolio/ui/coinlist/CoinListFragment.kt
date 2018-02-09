package pl.karass32.xfolio.ui.coinlist

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.coin_global_data_layout.view.*
import kotlinx.android.synthetic.main.coin_list_fragment.view.*
import pl.karass32.xfolio.MainActivity
import pl.karass32.xfolio.R
import pl.karass32.xfolio.adapter.CoinRvAdapter
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.data.GlobalCoinData
import pl.karass32.xfolio.error.CoinListErrorEvent
import pl.karass32.xfolio.error.ErrorUtils
import pl.karass32.xfolio.util.NumberUtils

/**
 * Created by karas on 14.01.2018.
 */
class CoinListFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var mView: View
    private lateinit var mCoinRvAdapter: CoinRvAdapter

    private val mViewModel: CoinListViewModel by lazy {
        ViewModelProviders.of(mainActivity).get(CoinListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.coin_list_fragment, container, false)

        setHasOptionsMenu(true)

        mainActivity = activity as MainActivity
        mainActivity.setSupportActionBar(mView.toolbar)
        mainActivity.supportActionBar?.setDisplayShowHomeEnabled(true)
        mainActivity.supportActionBar?.title = getString(R.string.nav_all_coins)
        mainActivity.setToggle(mView.toolbar)

        initSwipeRefreshLayout()
        initViewModel()

        return mView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.coin_list_menu, menu)

        val searchItem = menu.findItem(R.id.search_item)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.toolbar_menu_search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mCoinRvAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mCoinRvAdapter.filter.filter(newText)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initSwipeRefreshLayout() {
        mView.coinListSwipeRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE)
        mView.coinListSwipeRefresh.setOnRefreshListener {
            mViewModel.loadCoinList()
            mViewModel.loadGlobalCoinData()
        }
    }

    private fun initViewModel() {
        mViewModel.getCoinList()?.observe(this, Observer { coinList ->
            setCoinListSpinnerVisible(false)
            coinList?.let { showList(it) }
        })
        mViewModel.getGlobalCoinData()?.observe(this, Observer { globalCoinData ->
            globalCoinData?.let {
                showGlobalCoinData(it) }
        })
        mViewModel.coinListError.observe(this, Observer { error ->
            error?.let {
                setCoinListSpinnerVisible(false)
                onCoinListError(it) }
        })
    }

    private fun setCoinListSpinnerVisible(enable: Boolean) {
        mView.coinListSwipeRefresh.isRefreshing = enable
    }

    @SuppressLint("SetTextI18n")
    private fun showGlobalCoinData(globalCoinData: GlobalCoinData) {
        mView.headerTotalMarketCapValue.text = "$${NumberUtils.bigValueFormat.format(globalCoinData.totalMarketCap)}"
        mView.headerTotal24hVolumeValue.text = "$${NumberUtils.bigValueFormat.format(globalCoinData.total24hVolume)}"
        mView.headerBitcoinDominanceValue.text = "${NumberUtils.percentageFormat.format(globalCoinData.bitcoinDominance)}%"
        mView.headerActiveCurrenciesValue.text = globalCoinData.activeCurrencies.toString()
        mView.headerActiveAssetsValue.text = globalCoinData.activeAssets.toString()
        mView.headerActiveMarketsValue.text = globalCoinData.activeMarkets.toString()
    }

    private fun showList(list: ArrayList<CoinData>) {
        mCoinRvAdapter = CoinRvAdapter(list)
        mView.coinListRv?.setHasFixedSize(true)
        mView.coinListRv?.layoutManager = LinearLayoutManager(mainActivity)
        mView.coinListRv?.adapter = mCoinRvAdapter
    }

    private fun onCoinListError(error: CoinListErrorEvent) {
        Toast.makeText(context, ErrorUtils.getErrorString(context, error), Toast.LENGTH_LONG).show()
    }
}