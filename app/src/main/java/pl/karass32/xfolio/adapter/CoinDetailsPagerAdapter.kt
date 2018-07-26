package pl.karass32.xfolio.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import pl.karass32.xfolio.ui.coindetails.AlarmsFragment
import pl.karass32.xfolio.ui.coindetails.DetailsFragment
import pl.karass32.xfolio.ui.coindetails.MarketsFragment

class CoinDetailsPagerAdapter(fm: FragmentManager, private var mNumOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> DetailsFragment()
            1 -> MarketsFragment()
            2 -> AlarmsFragment()
            else -> null
        }
    }

    override fun getCount(): Int = mNumOfTabs
}