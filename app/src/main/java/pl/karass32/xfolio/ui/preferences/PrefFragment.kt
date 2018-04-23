package pl.karass32.xfolio.ui.preferences

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import pl.karass32.xfolio.MainActivity
import pl.karass32.xfolio.MyApplication
import pl.karass32.xfolio.R

class PrefFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val mainActivity: MainActivity by lazy { activity as MainActivity }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)


        val appLanguage = findPreference("general_app_language") as ListPreference
        appLanguage.summary = appLanguage.entry
        val autoOpen = findPreference("general_auto_open") as ListPreference
        autoOpen.summary = autoOpen.entry
        val defaultCurrency = findPreference("general_default_currency") as ListPreference
        val coinListOrder = findPreference("coin_list_order") as ListPreference
        coinListOrder.summary = coinListOrder.entry
        val coinListChange = findPreference("coin_list_change") as ListPreference
        coinListChange.summary = getString(R.string.pref_list_change_summary, coinListChange.entry)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val root = view.rootView as LinearLayout
        val bar = LayoutInflater.from(view.context).inflate(R.layout.settings_toolbar, root, false) as AppBarLayout
        root.addView(bar, 0)

        val toolbar = bar.getChildAt(0) as Toolbar
        toolbar.title = getString(R.string.nav_settings)
        toolbar.setNavigationOnClickListener { mainActivity.onBackPressed() }
        mainActivity.lockDrawerLayout(true)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view.setBackgroundColor(Color.WHITE)
        view.isClickable = true
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val preference = findPreference(key)
        if (preference is ListPreference) {
            preference.summary = if (preference.key != "coin_list_change") preference.entry else getString(R.string.pref_list_change_summary, preference.entry)
        }
        if (preference.key == "general_app_language") view.postDelayed({MyApplication.instance.restartApp()}, 200)
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