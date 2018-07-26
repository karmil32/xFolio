package pl.karass32.xfolio.ui.coindetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.karass32.xfolio.R
import pl.karass32.xfolio.base.BaseFragment

class MarketsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.coin_details_markets_fragment, container, false)
        return mView
    }
}