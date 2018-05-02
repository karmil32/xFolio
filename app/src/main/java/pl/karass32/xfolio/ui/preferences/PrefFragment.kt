package pl.karass32.xfolio.ui.preferences

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.mynameismidori.currencypicker.CurrencyPicker
import com.mynameismidori.currencypicker.CurrencyPreference
import com.mynameismidori.currencypicker.ExtendedCurrency
import pl.karass32.xfolio.MyApplication
import pl.karass32.xfolio.R
import pl.karass32.xfolio.repository.pref.SharedPreferencesRepositoryImpl

class PrefFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val settingsActivity: SettingsActivity by lazy { activity as SettingsActivity }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)

        val appLanguage = findPreference("general_app_language") as ListPreference
        appLanguage.summary = appLanguage.entry
        val autoOpen = findPreference("general_auto_open") as ListPreference
        autoOpen.summary = autoOpen.entry


        val supportedCurrencies = listOf("USD", "EUR", "PLN")
        val currencies: ArrayList<ExtendedCurrency> = ArrayList()
        currencies.apply { supportedCurrencies.forEach { currencies.add(ExtendedCurrency.getCurrencyByISO(it)) } }
        val defaultCurrency = findPreference("general_default_currency") as CurrencyPreference
        defaultCurrency.setCurrenciesList(currencies)
        val currencyPicker = CurrencyPicker.newInstance("Select Currency")
        currencyPicker.setCurrenciesList(currencies)
        currencyPicker.setListener { name, code, symbol, p3 -> defaultCurrency.value = code }

        val coinListOrder = findPreference("coin_list_order") as ListPreference
        coinListOrder.summary = coinListOrder.entry
        val coinListChange = findPreference("coin_list_change") as ListPreference
        coinListChange.summary = getString(R.string.pref_list_change_summary, coinListChange.entry)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val root = view?.rootView as LinearLayout
        val bar = LayoutInflater.from(view.context).inflate(R.layout.settings_toolbar, root, false) as AppBarLayout
        root.addView(bar, 0)

        val toolbar = bar.getChildAt(0) as Toolbar
        toolbar.title = getString(R.string.nav_settings)
        toolbar.setNavigationOnClickListener { settingsActivity.onBackPressed() }

        return view
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val preference = findPreference(key)
        if (preference is ListPreference) {
            preference.summary = if (preference.key != "coin_list_change") preference.entry else getString(R.string.pref_list_change_summary, preference.entry)
        }
        if (preference.key == SharedPreferencesRepositoryImpl.APP_LANGUAGE || preference.key == SharedPreferencesRepositoryImpl.NIGHT_MODE) view?.postDelayed({MyApplication.instance.restartApp()}, 200)
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