package pl.karass32.xfolio.ui.coindetails

import android.os.Bundle
import kotlinx.android.synthetic.main.coin_details_activity.*
import pl.karass32.xfolio.R
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
        supportFragmentManager.beginTransaction().replace(R.id.contentFrame, DetailsFragment()).commit()
    }
}