package pl.karass32.xfolio.ui.coindetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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
        initPriceChart()
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

    fun initPriceChart() {
        val list: ArrayList<Entry> = ArrayList()
        list.add(Entry(00f, 2.25f))
        list.add(Entry(01f, 2.28f))
        list.add(Entry(02f, 2.50f))
        list.add(Entry(03f, 2.98f))
        list.add(Entry(04f, 2.70f))
        list.add(Entry(05f, 2.75f))
        list.add(Entry(06f, 3.05f))
        list.add(Entry(07f, 3.15f))
        list.add(Entry(08f, 3.25f))
        list.add(Entry(09f, 3.33f))
        list.add(Entry(10f, 3.25f))
        list.add(Entry(11f, 3.11f))
        list.add(Entry(11f, 3.03f))
        list.add(Entry(12f, 2.99f))
        list.add(Entry(13f, 3.14f))
        list.add(Entry(14f, 3.28f))
        list.add(Entry(15f, 3.36f))
        list.add(Entry(16f, 3.42f))
        list.add(Entry(17f, 3.44f))
        list.add(Entry(18f, 3.68f))
        list.add(Entry(19f, 3.77f))
        list.add(Entry(20f, 3.83f))
        list.add(Entry(21f, 3.79f))
        list.add(Entry(22f, 3.89f))
        list.add(Entry(23f, 3.96f))
        list.add(Entry(24f, 3.71f))

        val lineDataSet = LineDataSet(list, "Guwno")
        val lineData = LineData(lineDataSet)
        priceChart.data = lineData
        priceChart.animateX(1000)
        priceChart.xAxis.isEnabled = false
        priceChart.invalidate()
    }
}