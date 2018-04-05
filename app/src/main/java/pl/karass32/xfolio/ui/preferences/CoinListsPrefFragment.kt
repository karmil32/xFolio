package pl.karass32.xfolio.ui.preferences

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import pl.karass32.xfolio.R

class CoinListsPrefFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.coin_lists_prefs)

        val coinListOrder = findPreference("coin_list_order") as ListPreference
        coinListOrder.summary = coinListOrder.entry
        val coinListChange = findPreference("coin_list_change") as ListPreference
        coinListChange.summary = getString(R.string.pref_list_change_summary, coinListChange.entry)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val preference = findPreference(key)
        if (preference is ListPreference) {
            preference.summary = if (preference.key != "coin_list_change") preference.entry else getString(R.string.pref_list_change_summary, preference.entry)
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }
}