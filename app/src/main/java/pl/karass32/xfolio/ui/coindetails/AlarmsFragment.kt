package pl.karass32.xfolio.ui.coindetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import kotlinx.android.synthetic.main.coin_details_alarms_fragment.*
import kotlinx.android.synthetic.main.coin_details_alarms_fragment.view.*
import pl.karass32.xfolio.R
import pl.karass32.xfolio.base.BaseFragment
import pl.karass32.xfolio.listener.RepeatListener
import pl.karass32.xfolio.util.CurrencyUtils
import java.math.BigDecimal
import java.math.RoundingMode

class AlarmsFragment : BaseFragment() {

    lateinit var mActivity: CoinDetailsActivity
    lateinit var mViewModel: CoinDetailsViewModel

    lateinit var mCoinSymbol: String

    lateinit var mCurrentPrice: BigDecimal

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.coin_details_alarms_fragment, container, false)

        activity?.let {
            mActivity = it as CoinDetailsActivity
            mCoinSymbol = mActivity.mCoinSymbol
        }
        mViewModel = ViewModelProviders.of(mActivity).get(CoinDetailsViewModel::class.java)

        initViewModel()
        initOnClicks()

        return mView
    }

    private fun initViewModel() {
        mViewModel.getCoinData(mCoinSymbol)?.observe(this, Observer { coinData ->
            val currencyCode = preferences.getDefaultCurrency()
            coinData?.price?.let {
                mCurrentPrice = it.setScale(2, RoundingMode.UP)
                if (mView.priceAboveEditText.text.isEmpty()) {
                    mView.priceAboveEditText.hint = mCurrentPrice.toString()
                }
                mView.currentPrice.text = CurrencyUtils.getFormattedPrice(it, currencyCode)
            }
        })
    }

    private fun initOnClicks() {
        mView.priceAboveSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            TransitionManager.beginDelayedTransition(priceAboveCardView)
            mView.priceAboveClearButton.visibility = if (isChecked) VISIBLE else GONE
            mView.priceAbovePlusButton.visibility = if (isChecked) VISIBLE else GONE
            mView.priceAboveMinusButton.visibility = if (isChecked) VISIBLE else GONE
            mView.priceAboveEditText.visibility = if (isChecked) VISIBLE else GONE
        }
        mView.priceAboveClearButton.setOnClickListener { mView.priceAboveEditText.setText("") }

        mView.priceAbovePlusButton.setOnClickListener {
            if (priceAboveEditText.text.isEmpty()) {
                priceAboveEditText.setText(mCurrentPrice.toPlainString())
            }
            val step = mCurrentPrice.multiply(BigDecimal("0.001"))
            val increasedPrice = BigDecimal(priceAboveEditText.text.toString()).plus(step)
            priceAboveEditText.setText(increasedPrice.setScale(2, RoundingMode.UP).toPlainString())
        }
        mView.priceAbovePlusButton.setOnTouchListener(object : RepeatListener(400, 100, View.OnClickListener {
            if (priceAboveEditText.text.isEmpty()) {
                priceAboveEditText.setText(mCurrentPrice.toPlainString())
            }
            val step = mCurrentPrice.multiply(BigDecimal("0.01"))
            val increasedPrice = BigDecimal(priceAboveEditText.text.toString()).plus(step)
            priceAboveEditText.setText(increasedPrice.setScale(2, RoundingMode.UP).toPlainString())
        }){})
        mView.priceAboveMinusButton.setOnClickListener {
            if (priceAboveEditText.text.isEmpty()) {
                priceAboveEditText.setText(mCurrentPrice.toPlainString())
            }
            val step = mCurrentPrice.multiply(BigDecimal("0.001"))
            val decreasedPrice = BigDecimal(priceAboveEditText.text.toString()).minus(step)
            priceAboveEditText.setText(decreasedPrice.setScale(2, RoundingMode.UP).toPlainString())
        }
        mView.priceAboveMinusButton.setOnTouchListener(object : RepeatListener(400, 100, View.OnClickListener {
            if (priceAboveEditText.text.isEmpty()) {
                priceAboveEditText.setText(mCurrentPrice.toPlainString())
            }
            val step = mCurrentPrice.multiply(BigDecimal("0.01"))
            val decreasedPrice = BigDecimal(priceAboveEditText.text.toString()).minus(step)
            priceAboveEditText.setText(decreasedPrice.setScale(2, RoundingMode.UP).toPlainString())
        }){})
    }
}