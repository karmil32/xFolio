package pl.karass32.xfolio.ui.portfolio

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.karass32.xfolio.MainActivity
import pl.karass32.xfolio.R

/**
 * Created by karas on 14.01.2018.
 */
class PortfolioFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.portfolio_fragment, container, false)

        val mainActivity = activity as MainActivity
        val actionBar = mainActivity.supportActionBar
        actionBar?.title = getString(R.string.nav_portfolio)

        return view
    }
}