package pl.karass32.xfolio.ui.coindetails

import android.os.Bundle
import android.support.design.widget.TabLayout
import kotlinx.android.synthetic.main.coin_details_activity.*
import pl.karass32.xfolio.R
import pl.karass32.xfolio.adapter.CoinDetailsPagerAdapter
import pl.karass32.xfolio.base.BaseActivity

class CoinDetailsActivity : BaseActivity() {

    lateinit var mCoinSymbol: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coin_details_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val args = intent.extras
        args?.let {
            mCoinSymbol = args.getString("COIN_SYMBOL")
        }

        val viewPagerAdapter = CoinDetailsPagerAdapter(supportFragmentManager, detailsTabs.tabCount)
        detailsViewPager.adapter = viewPagerAdapter
        detailsViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(detailsTabs))
        detailsTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                detailsViewPager.currentItem = tab.position
            }
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
        })
    }
}