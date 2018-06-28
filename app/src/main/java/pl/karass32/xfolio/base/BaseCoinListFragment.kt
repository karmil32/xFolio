package pl.karass32.xfolio.base

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import com.mynameismidori.currencypicker.CurrencyPicker
import com.mynameismidori.currencypicker.ExtendedCurrency
import kotlinx.android.synthetic.main.coin_list_fragment.*
import kotlinx.android.synthetic.main.coin_list_fragment.view.*
import kotlinx.android.synthetic.main.coin_rv_header_layout.view.*
import pl.karass32.xfolio.MainActivity
import pl.karass32.xfolio.R
import pl.karass32.xfolio.adapter.CoinRvAdapter
import pl.karass32.xfolio.decoration.MyDividerItemDecoration
import pl.karass32.xfolio.error.CoinListErrorEvent
import pl.karass32.xfolio.error.ErrorUtils
import pl.karass32.xfolio.util.CoinOrder
import pl.karass32.xfolio.util.enum.ChangeOption

abstract class BaseCoinListFragment : BaseFragment() {

    val SEARCH_QUERY_STATE = "SEARCH_QUERY_STATE"

    var mSearchQueryState = ""

    val mainActivity: MainActivity by lazy { activity as MainActivity }
    lateinit var mCoinRvAdapter: CoinRvAdapter

    lateinit var mViewModel: BaseCoinListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        savedInstanceState?.let {
            if (savedInstanceState.containsKey(SEARCH_QUERY_STATE)) {
                mSearchQueryState = savedInstanceState.getString(SEARCH_QUERY_STATE)
            }
        }

        initToolbar()
        initViewModel()
        initRv()
        initSwipeRefreshLayout()
        initChangeSpinner()
        initCurrencyPicker()
        initScrollUpButton()

        return mView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (!mSearchQueryState.isEmpty()) outState.putString(SEARCH_QUERY_STATE, mSearchQueryState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.coin_list_menu, menu)

        val listOrderMethod = CoinOrder.of(preferences.getCoinListOrder())
        val orderItem = when(listOrderMethod) {
            CoinOrder.BY_MARKET_CAP_DSC -> menu.findItem(R.id.sort_by_market_cap_dsc)
            CoinOrder.BY_MARKET_CAP_ASC -> menu.findItem(R.id.sort_by_market_cap_asc)
            CoinOrder.BY_PRICE_DSC -> menu.findItem(R.id.sort_by_price_dsc)
            CoinOrder.BY_PRICE_ASC -> menu.findItem(R.id.sort_by_price_asc)
            CoinOrder.BY_CHANGE_1H_DSC -> menu.findItem(R.id.sort_by_change_1h_dsc)
            CoinOrder.BY_CHANGE_1H_ASC -> menu.findItem(R.id.sort_by_change_1h_asc)
            CoinOrder.BY_CHANGE_24H_DSC -> menu.findItem(R.id.sort_by_change_24h_dsc)
            CoinOrder.BY_CHANGE_24H_ASC -> menu.findItem(R.id.sort_by_change_24h_asc)
        }
        orderItem.isChecked = true

        val searchItem = menu.findItem(R.id.search_item)
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                menu.removeItem(R.id.sort_item)
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                mSearchQueryState = ""
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
                mSearchQueryState = newText
                return true
            }
        })

        val query = mSearchQueryState
        if (!query.isEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(query, false)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.sort_by_market_cap_dsc -> mViewModel.changeListOrder(CoinOrder.BY_MARKET_CAP_DSC)
            R.id.sort_by_market_cap_asc -> mViewModel.changeListOrder(CoinOrder.BY_MARKET_CAP_ASC)
            R.id.sort_by_price_dsc -> mViewModel.changeListOrder(CoinOrder.BY_PRICE_DSC)
            R.id.sort_by_price_asc -> mViewModel.changeListOrder(CoinOrder.BY_PRICE_ASC)
            R.id.sort_by_change_1h_dsc -> mViewModel.changeListOrder(CoinOrder.BY_CHANGE_1H_DSC)
            R.id.sort_by_change_1h_asc -> mViewModel.changeListOrder(CoinOrder.BY_CHANGE_1H_ASC)
            R.id.sort_by_change_24h_dsc -> mViewModel.changeListOrder(CoinOrder.BY_CHANGE_24H_DSC)
            R.id.sort_by_change_24h_asc -> mViewModel.changeListOrder(CoinOrder.BY_CHANGE_24H_ASC)
        }
        item?.isChecked = true
        return super.onOptionsItemSelected(item)
    }

    open fun initToolbar() {
        with(mainActivity) {
            setSupportActionBar(mView.toolbar)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            setToggle(mView.toolbar)
        }
    }

    open fun initSwipeRefreshLayout() {
        with(mView.coinListSwipeRefresh) {
            setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE)
            setOnRefreshListener {
                mViewModel.loadLatestData()
            }
        }
    }

    open fun initViewModel() {
        mViewModel.getCoinList()?.observe(this, Observer { coinList ->
            coinList?.let {
                mCoinRvAdapter.submitList(it)
                mView.coinListRv?.adapter = mCoinRvAdapter
                mView.rvError.visibility = View.GONE
            }
        })
        mViewModel.coinListError.observe(this, Observer { error ->
            error?.let { onCoinListError(it) }
        })
        mViewModel.isLoading.observe(this, Observer { isLoading ->
            isLoading?.let { setLoadingSpinnerVisible(isLoading) }
        })
    }

    open fun initRv() {
        mView.coinListRv?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(mainActivity)
            addItemDecoration(MyDividerItemDecoration(appContext, DividerItemDecoration.VERTICAL, 10))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
                    if (layoutManager.findFirstCompletelyVisibleItemPosition() > 5)
                        mView.topScrollFab.show() else
                        mView.topScrollFab.hide()
                }
            })
            mCoinRvAdapter = CoinRvAdapter(mainActivity, mViewModel)
        }
    }

    open fun initChangeSpinner() {
        with(mView.headerChangeSpinner) {
            setSelection(ChangeOption.of(preferences.getCoinListDefaultChange()).ordinal, false)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val changeType = ChangeOption.values().first { it.ordinal == p2 }
                    preferences.setCoinListDefaultChange(changeType.value)
                    mCoinRvAdapter.notifyDataSetChanged()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    open fun initCurrencyPicker() {
        val picker = CurrencyPicker.newInstance("Select Currency")
        picker.setListener { name, code, symbol, flag ->
            mViewModel.setCurrency(code)
            mView.headerCurrencyPicker.text = code
            picker.dialog.cancel()
        }
        val supportedCurrencies = listOf("USD", "EUR", "PLN")
        val currencies: ArrayList<ExtendedCurrency> = ArrayList()

        currencies.apply { supportedCurrencies.forEach { currencies.add(ExtendedCurrency.getCurrencyByISO(it)) } }
        picker.setCurrenciesList(currencies)

        mView.headerCurrencyPicker.text = preferences.getDefaultCurrency()
        mView.headerCurrencyPicker.setOnClickListener { picker.show(fragmentManager, "CURRENCY_PICKER") }
    }

    open fun initScrollUpButton() {
        mView.topScrollFab.setOnClickListener {
            mView.appbarLayout.setExpanded(true)
            val layoutManager = coinListRv?.layoutManager as LinearLayoutManager
            if (layoutManager.findFirstCompletelyVisibleItemPosition() > 35) coinListRv.scrollToPosition(30)
            coinListRv.smoothScrollToPosition(0)
        }
    }

    private fun setLoadingSpinnerVisible(enable: Boolean) {
        mView.coinListSwipeRefresh.isRefreshing = enable
    }

    open fun onCoinListError(error: CoinListErrorEvent) {
        val errorString = ErrorUtils.getErrorString(appContext, error)
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
        mView.rvError.text = errorString
    }
}