package pl.karass32.xfolio.ui.coinlist

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.coin_global_data_layout.view.*
import kotlinx.android.synthetic.main.coin_list_fragment.*
import kotlinx.android.synthetic.main.coin_list_fragment.view.*
import kotlinx.android.synthetic.main.coin_rv_header_layout.view.*
import org.joda.time.DateTime
import pl.karass32.xfolio.MainActivity
import pl.karass32.xfolio.R
import pl.karass32.xfolio.adapter.CoinRvAdapter
import pl.karass32.xfolio.base.BaseFragment
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.data.GlobalCoinData
import pl.karass32.xfolio.decoration.MyDividerItemDecoration
import pl.karass32.xfolio.error.CoinListErrorEvent
import pl.karass32.xfolio.error.ErrorUtils
import pl.karass32.xfolio.util.NumberUtils
import pl.karass32.xfolio.util.CoinOrder
import pl.karass32.xfolio.util.enum.ChangeOption
import timber.log.Timber

/**
 * Created by karas on 14.01.2018.
 */
class CoinListFragment : BaseFragment() {

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

        initViewModel()
        initRv()
        initSwipeRefreshLayout()
        initHeaderSpinners()

        mView.topScrollFab.setOnClickListener {
            mView.appbarLayout.setExpanded(true)
            val layoutManager = coinListRv?.layoutManager as LinearLayoutManager
            if (layoutManager.findFirstCompletelyVisibleItemPosition() > 35) coinListRv.scrollToPosition(30)
            coinListRv.smoothScrollToPosition(0)
        }

        return mView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.coin_list_menu, menu)

        val searchItem = menu.findItem(R.id.search_item)
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                menu.removeItem(R.id.sort_item)
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                menu.clear()
                onCreateOptionsMenu(menu, inflater)
                return true
            }
        })

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.sort_by_market_cap_dsc -> mViewModel.sortCoinList(CoinOrder.BY_MARKET_CAP_DSC)
            R.id.sort_by_market_cap_asc -> mViewModel.sortCoinList(CoinOrder.BY_MARKET_CAP_ASC)
            R.id.sort_by_price_dsc -> mViewModel.sortCoinList(CoinOrder.BY_PRICE_DSC)
            R.id.sort_by_price_asc -> mViewModel.sortCoinList(CoinOrder.BY_PRICE_ASC)
            R.id.sort_by_change_1h_dsc -> mViewModel.sortCoinList(CoinOrder.BY_CHANGE_1H_DSC)
            R.id.sort_by_change_1h_asc -> mViewModel.sortCoinList(CoinOrder.BY_CHANGE_1H_ASC)
            R.id.sort_by_change_24h_dsc -> mViewModel.sortCoinList(CoinOrder.BY_CHANGE_24H_DSC)
            R.id.sort_by_change_24h_asc -> mViewModel.sortCoinList(CoinOrder.BY_CHANGE_24H_ASC)
        }
        item?.isChecked = true
        return super.onOptionsItemSelected(item)
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
            coinList?.let {
                showList(it)
                mView.rvError.visibility = View.GONE
            }
        })
        mViewModel.getGlobalCoinData()?.observe(this, Observer { globalCoinData ->
            globalCoinData?.let { showGlobalCoinData(it) }
        })
        mViewModel.getFiatStringCodes()?.observe(this, Observer { fiatCodes ->
            val fiatArray = fiatCodes?.plusElement("USD")
            val adapter = ArrayAdapter<String>(appContext, android.R.layout.simple_spinner_item, fiatArray)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mView.headerCurrencySpinner.adapter = adapter
        })
        mViewModel.coinListError.observe(this, Observer { error ->
            error?.let { onCoinListError(it) }
        })
        mViewModel.isLoading.observe(this, Observer { isLoading ->
            isLoading?.let { setCoinListSpinnerVisible(isLoading) }
        })
        mViewModel.currency.observe(this, Observer { rate ->
            Timber.d("Rate: " + rate)
        })
    }

    private fun initRv() {
        mView.coinListRv?.setHasFixedSize(true)
        mView.coinListRv?.layoutManager = LinearLayoutManager(mainActivity)
        mView.coinListRv.addItemDecoration(MyDividerItemDecoration(appContext, DividerItemDecoration.VERTICAL, 10))
        mView.coinListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
                if (layoutManager.findFirstCompletelyVisibleItemPosition() > 5)
                    topScrollFab.show() else
                    topScrollFab.hide()
            }
        })
    }

    private fun initHeaderSpinners() {
        mView.headerChangeSpinner.setSelection(1, false)
        mView.headerChangeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                CoinRvAdapter.changeType = ChangeOption.values().first {it.value == p2}
                mCoinRvAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        mView.headerCurrencySpinner.setSelection(0, false)
        mView.headerCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                (p0?.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                val currencyCode = p0?.getItemAtPosition(p2).toString()
                mViewModel.setCurrency(currencyCode)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setCoinListSpinnerVisible(enable: Boolean) {
        mView.coinListSwipeRefresh.isRefreshing = enable
    }

    @SuppressLint("SetTextI18n")
    private fun showGlobalCoinData(globalCoinData: GlobalCoinData) {
        mView.headerTotalMarketCap.text = "$${NumberUtils.bigValueFormat.format(globalCoinData.totalMarketCap)}"
        mView.headerTotal24hVolume.text = "$${NumberUtils.bigValueFormat.format(globalCoinData.total24hVolume)}"
        mView.headerBitcoinDominance.text = "${NumberUtils.percentageFormat.format(globalCoinData.bitcoinDominance)}%"
        mView.headerActiveCurrencies.text = globalCoinData.activeCurrencies.toString()
        mView.headerActiveAssets.text = globalCoinData.activeAssets.toString()
        mView.headerActiveMarkets.text = globalCoinData.activeMarkets.toString()
        mView.headerLastUpdated.text = NumberUtils.dateTimeFormat.print(DateTime(globalCoinData.lastUpdated * 1000)).toString()
    }

    private fun showList(list: List<CoinData>) {
        mCoinRvAdapter = CoinRvAdapter(list)
        mView.coinListRv?.adapter = mCoinRvAdapter
    }

    private fun onCoinListError(error: CoinListErrorEvent) {
        val errorString = ErrorUtils.getErrorString(appContext, error)
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
        mView.rvError.visibility = View.VISIBLE
        mView.rvError.text = errorString
    }
}