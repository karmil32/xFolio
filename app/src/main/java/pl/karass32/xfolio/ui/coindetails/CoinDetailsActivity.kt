package pl.karass32.xfolio.ui.coindetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import kotlinx.android.synthetic.main.coin_details_activity.*
import pl.karass32.xfolio.R
import pl.karass32.xfolio.base.BaseActivity
import pl.karass32.xfolio.util.CurrencyUtils

class CoinDetailsActivity : BaseActivity() {

    lateinit var mViewModel: CoinDetailsViewModel
    lateinit var mCoinId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coin_details_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProviders.of(this).get(CoinDetailsViewModel::class.java)

        val args = intent.extras
        args?.let {
            mCoinId = args.getString("id")
        }

        initViewModel()
    }

    fun initViewModel() {
        mViewModel.getCoinData(mCoinId)?.observe(this, Observer { coinData ->
            supportActionBar?.title = coinData?.name

            val currencyCode = preferences.getDefaultCurrency()

            coinData?.price?.let { price.text = CurrencyUtils.getFormattedPrice(it, currencyCode) }
            rank.text = coinData?.rank?.toString()
            coinData?.marketCap?.let { marketCap.text = CurrencyUtils.getFormattedBigValue(it.toLong(), currencyCode) }
            coinData?.volume24h?.let { volume24h.text = CurrencyUtils.getFormattedBigValue(it.toLong(), currencyCode) }
            coinData?.availableSupply?.let { availableSupply.text = String.format("%s %s", CurrencyUtils.getFormattedBigValue(it, null), coinData.symbol) }
            coinData?.totalSupply?.let { maxSupply.text = String.format("%s %s", CurrencyUtils.getFormattedBigValue(it, null), coinData.symbol) }
        })
    }
}