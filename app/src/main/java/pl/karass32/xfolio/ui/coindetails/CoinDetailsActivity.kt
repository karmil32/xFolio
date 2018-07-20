package pl.karass32.xfolio.ui.coindetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.coin_details_activity.*
import pl.karass32.xfolio.R
import pl.karass32.xfolio.base.BaseActivity
import pl.karass32.xfolio.data.HistDataResponse
import pl.karass32.xfolio.util.AxisValueFormatter
import pl.karass32.xfolio.util.CurrencyUtils

class CoinDetailsActivity : BaseActivity() {

    lateinit var mViewModel: CoinDetailsViewModel
    lateinit var mCoinSymbol: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coin_details_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProviders.of(this).get(CoinDetailsViewModel::class.java)

        val args = intent.extras
        args?.let {
            mCoinSymbol = args.getString("COIN_SYMBOL")
        }

        initViewModel()
        initPriceChart()
    }

    fun initViewModel() {
        mViewModel.getCoinData(mCoinSymbol)?.observe(this, Observer { coinData ->
            supportActionBar?.title = coinData?.name

            val currencyCode = preferences.getDefaultCurrency()

            coinData?.price?.let { price.text = CurrencyUtils.getFormattedPrice(it, currencyCode) }
            rank.text = coinData?.rank?.toString()
            coinData?.marketCap?.let { marketCap.text = CurrencyUtils.getFormattedBigValue(it.toLong(), currencyCode) }
            coinData?.volume24h?.let { volume24h.text = CurrencyUtils.getFormattedBigValue(it.toLong(), currencyCode) }
            coinData?.availableSupply?.let { availableSupply.text = String.format("%s %s", CurrencyUtils.getFormattedBigValue(it, null), coinData.symbol) }
            coinData?.totalSupply?.let { maxSupply.text = String.format("%s %s", CurrencyUtils.getFormattedBigValue(it, null), coinData.symbol) }
        })
        mViewModel.getHistData(mCoinSymbol)?.observe(this, Observer { histData ->
            histData?.let { showPriceChart(it) }
        })

        mViewModel.isChartLoading.observe(this, Observer { isLoading ->
            isLoading?.let { chartProgressBar.visibility = if (isLoading) VISIBLE else GONE }
        })
    }

    fun initPriceChart() {
        priceChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        priceChart.xAxis.setDrawGridLines(false)
        priceChart.xAxis.setLabelCount(4, true)
        priceChart.setNoDataText("")
        priceChart.description.isEnabled = false
        priceChart.legend.isEnabled = false
        priceChart.axisLeft.isEnabled = false

        initPriceChartRadioButtons()
    }

    fun initPriceChartRadioButtons() {
        chartAllRadioButton.isChecked = true
        chartRadioGroup.setOnCheckedChangeListener { radioGroup, id ->
            priceChart.clear()
            when (id) {
                R.id.chart1dRadioButton -> mViewModel.load1dHistData(mCoinSymbol)
                R.id.chart7dRadioButton -> mViewModel.load7dHistData(mCoinSymbol)
                R.id.chart1mRadioButton -> mViewModel.load1mHistData(mCoinSymbol)
                R.id.chart6mRadioButton -> mViewModel.load6mHistData(mCoinSymbol)
                R.id.chart1yRadioButton -> mViewModel.load1yHistData(mCoinSymbol)
                R.id.chartAllRadioButton -> mViewModel.loadAllHistData(mCoinSymbol)
            }
        }
    }

    fun showPriceChart(histData: HistDataResponse) {
        val list: ArrayList<Entry> = ArrayList()
        for (histEntry in histData.data) {
            list.add(Entry(histEntry.time.toFloat(), histEntry.close.toFloat()))
        }

        val timePeriod = list.last().x.toLong() - list.first().x.toLong()
        priceChart.xAxis.valueFormatter = AxisValueFormatter(timePeriod)

        val lineDataSet = LineDataSet(list, null)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawFilled(true)
        val lineData = LineData(lineDataSet)
        lineData.setDrawValues(false)
        priceChart.data = lineData
        priceChart.animateX(1000)
        priceChart.invalidate()
    }
}