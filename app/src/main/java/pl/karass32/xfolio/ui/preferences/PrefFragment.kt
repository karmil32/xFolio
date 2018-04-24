package pl.karass32.xfolio.ui.preferences

import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.preference.ListPreference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import pl.karass32.xfolio.MainActivity
import pl.karass32.xfolio.MyApplication
import pl.karass32.xfolio.R
import pl.karass32.xfolio.repository.pref.SharedPreferencesRepositoryImpl

class PrefFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val mainActivity: MainActivity by lazy { activity as MainActivity }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        val appLanguage = findPreference("general_app_language") as ListPreference
        appLanguage.summary = appLanguage.entry
        val autoOpen = findPreference("general_auto_open") as ListPreference
        autoOpen.summary = autoOpen.entry
        val defaultCurrency = findPreference("general_default_currency") as ListPreference
        val nightMode = findPreference("general_night_mode")
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
        toolbar.setNavigationOnClickListener { mainActivity.onBackPressed() }
        mainActivity.lockDrawerLayout(true)

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

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.lockDrawerLayout(false)
    }
}