package pl.karass32.xfolio.ui.coindetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.coin_details_fragment.view.*
import pl.karass32.xfolio.R
import pl.karass32.xfolio.base.BaseFragment
import pl.karass32.xfolio.data.HistDataResponse
import pl.karass32.xfolio.extension.getCompatColor
import pl.karass32.xfolio.util.AxisValueFormatter
import pl.karass32.xfolio.util.CurrencyUtils
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : BaseFragment(), OnChartValueSelectedListener {

    lateinit var mActivity: CoinDetailsActivity
    lateinit var mViewModel: CoinDetailsViewModel

    lateinit var mCoinSymbol: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.coin_details_fragment, container, false)
        activity?.let {
            mActivity = it as CoinDetailsActivity
            mCoinSymbol = mActivity.mCoinSymbol
        }
        mViewModel = ViewModelProviders.of(mActivity).get(CoinDetailsViewModel::class.java)

        initViewModel()
        initPriceChart()

        return mView
    }


    fun initViewModel() {
        mViewModel.getCoinData(mCoinSymbol)?.observe(this, Observer { coinData ->
            mActivity.supportActionBar?.title = coinData?.name

            val currencyCode = preferences.getDefaultCurrency()

            coinData?.price?.let { mView.price.text = CurrencyUtils.getFormattedPrice(it, currencyCode) }
            mView.rank.text = coinData?.rank?.toString()
            coinData?.marketCap?.let { mView.marketCap.text = CurrencyUtils.getFormattedBigValue(it.toLong(), currencyCode) }
            coinData?.volume24h?.let { mView.volume24h.text = CurrencyUtils.getFormattedBigValue(it.toLong(), currencyCode) }
            coinData?.availableSupply?.let { mView.availableSupply.text = String.format("%s %s", CurrencyUtils.getFormattedBigValue(it, null), coinData.symbol) }
            coinData?.totalSupply?.let { mView.maxSupply.text = String.format("%s %s", CurrencyUtils.getFormattedBigValue(it, null), coinData.symbol) }
            coinData?.change1h?.let {
                mView.change1h.text = appContext.getString(R.string.percent_template, it.toString())
                mView.change1h.setTextColor(if (it >= BigDecimal(0)) getCompatColor(R.color.positiveColor) else getCompatColor(R.color.negativeColor))
            }
            coinData?.change24h?.let {
                mView.change24h.text = appContext.getString(R.string.percent_template, it.toString())
                mView.change24h.setTextColor(if (it >= BigDecimal(0)) getCompatColor(R.color.positiveColor) else getCompatColor(R.color.negativeColor))
            }
            coinData?.change7d?.let {
                mView.change7d.text = appContext.getString(R.string.percent_template, it.toString())
                mView.change7d.setTextColor(if (it >= BigDecimal(0)) getCompatColor(R.color.positiveColor) else getCompatColor(R.color.negativeColor))
            }
        })
        mViewModel.getHistData(mCoinSymbol)?.observe(this, Observer { histData ->
            histData?.let { showPriceChart(it) }
        })

        mViewModel.isChartLoading.observe(this, Observer { isLoading ->
            isLoading?.let { mView.chartProgressBar.visibility = if (isLoading) VISIBLE else GONE }
        })
    }

    fun initPriceChart() {
        mView.priceChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        mView.priceChart.xAxis.setDrawGridLines(false)
        mView.priceChart.xAxis.setLabelCount(4, true)
        mView.priceChart.setNoDataText("")
        mView.priceChart.description.isEnabled = false
        mView.priceChart.legend.isEnabled = false
        mView.priceChart.axisLeft.isEnabled = false
        mView.priceChart.setOnChartValueSelectedListener(this)

        initPriceChartRadioButtons()
    }

    fun initPriceChartRadioButtons() {
        mView.chartAllRadioButton.isChecked = true
        mView.chartRadioGroup.setOnCheckedChangeListener { radioGroup, id ->
            mView.priceChart.clear()
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
        mView.priceChart.xAxis.valueFormatter = AxisValueFormatter(timePeriod)

        val lineDataSet = LineDataSet(list, null)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawFilled(true)
        val lineData = LineData(lineDataSet)
        lineData.setDrawValues(false)
        mView.priceChart.data = lineData
        mView.priceChart.animateX(1000)
        mView.priceChart.highlightValue(Highlight(lineData.xMax, 0 ,0), true)
        mView.priceChart.invalidate()
    }

    override fun onNothingSelected() {}

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        val currencyCode = preferences.getDefaultCurrency()
        val format = SimpleDateFormat("dd MMM yyyy HH:mm ")

        val price = CurrencyUtils.getFormattedPrice(BigDecimal(e?.y.toString()), currencyCode)
        mView.chartPriceTextView.text = price
        mView.chartDateTextView.text = e?.x?.let { format.format(it * 1000) }
    }
}