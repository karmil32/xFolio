package pl.karass32.xfolio.ui.preferences

import android.preference.PreferenceActivity
import pl.karass32.xfolio.R

class PrefActivity : PreferenceActivity() {

    override fun onBuildHeaders(target: MutableList<Header>?) {
        loadHeadersFromResource(R.xml.headers_preference, target)
    }

    override fun isValidFragment(fragmentName: String?): Boolean {
        return CoinListPrefFragment::class.java.name == fragmentName
    }
}