package pl.karass32.xfolio.ui.preferences

import android.os.Bundle
import android.preference.PreferenceFragment
import pl.karass32.xfolio.R

class CoinListPrefFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}