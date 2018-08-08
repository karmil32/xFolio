package pl.karass32.xfolio.ui.coindetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.transition.TransitionManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import kotlinx.android.synthetic.main.coin_details_alarms_fragment.*
import kotlinx.android.synthetic.main.coin_details_alarms_fragment.view.*
import org.jetbrains.anko.support.v4.longToast
import pl.karass32.xfolio.R
import pl.karass32.xfolio.base.BaseFragment
import pl.karass32.xfolio.extension.clearText
import pl.karass32.xfolio.extension.percentageDiff
import pl.karass32.xfolio.extension.valueDiff
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
        initListeners()

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
        mView.priceAboveClearButton.setOnClickListener {
            mView.priceAboveEditText.text.clear()
            mView.priceAbovePrcValChange.clearText()
        }

        mView.priceAbovePlusButton.setOnTouchListener(object : RepeatListener(400, 100, View.OnClickListener {
            if (priceAboveEditText.text.isEmpty()) {
                priceAboveEditText.setText(mCurrentPrice.toPlainString())
            }
            val step = mCurrentPrice.multiply(BigDecimal("0.01"))
            val increasedPrice = BigDecimal(priceAboveEditText.text.toString()).plus(step)
            priceAboveEditText.setText(increasedPrice.setScale(2, RoundingMode.UP).toPlainString())
        }){})
        mView.priceAboveMinusButton.setOnTouchListener(object : RepeatListener(400, 100, View.OnClickListener {
            if (priceAboveEditText.text.isEmpty()) {
                priceAboveEditText.setText(mCurrentPrice.toPlainString())
            }
            val step = mCurrentPrice.multiply(BigDecimal("0.01"))
            val decreasedPrice = BigDecimal(priceAboveEditText.text.toString()).minus(step)
            if (decreasedPrice > mCurrentPrice) {
                priceAboveEditText.setText(decreasedPrice.setScale(2, RoundingMode.UP).toPlainString())
            } else {
                priceAboveEditText.setText(mCurrentPrice.toPlainString())
                longToast(appContext.getString(R.string.coin_alarm_price_above_error))
//                Toast.makeText(appContext, appContext.getText(R.string.coin_alarm_price_above_error), Toast.LENGTH_LONG).show()
            }
        }){})
    }

    private fun initListeners() {
        mView.priceAboveEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!mView.priceAboveEditText.text.isEmpty()) {
                    val percentageChange = mCurrentPrice.percentageDiff(BigDecimal(p0.toString()))
                    val valueChange = mCurrentPrice.valueDiff(BigDecimal(p0.toString()))
                    mView.priceAbovePrcValChange.text = appContext.getString(R.string.coin_alarm_change_prc_val_template, percentageChange, CurrencyUtils.getFormattedPrice(valueChange, preferences.getDefaultCurrency()))

                    if (BigDecimal(mView.priceAboveEditText.text.toString()) < mCurrentPrice) {
                        mView.priceAboveEditText.error = appContext.getString(R.string.coin_alarm_price_above_error)
                    }
                }
            }
        })
    }
}