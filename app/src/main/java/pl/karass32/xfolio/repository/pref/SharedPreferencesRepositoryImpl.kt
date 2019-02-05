package pl.karass32.xfolio.repository.pref

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by karas on 08.03.2018.
 */
class SharedPreferencesRepositoryImpl(context: Context) : SharedPreferencesRepository {

    companion object {
        const val APP_LANGUAGE = "general_app_language"
        const val DEFAULT_CURRENCY = "general_default_currency"
        const val AUTO_OPEN = "general_auto_open"
        const val NIGHT_MODE = "general_night_mode"
        const val COIN_LIST_ORDER = "coin_list_order"
        const val COIN_LIST_DEFAULT_CHANGE = "coin_list_default_change"
    }

    private val preferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    override fun setLanguage(lang: String) = preferences.edit().putString(APP_LANGUAGE, lang).apply()
    override fun getLanguage(): String = preferences.getString(APP_LANGUAGE, "auto")

    override fun setAutoOpen(fragmentTag: String) = preferences.edit().putString(AUTO_OPEN, fragmentTag).apply()
    override fun getAutoOpen(): String = preferences.getString(AUTO_OPEN, "CoinListFragment")

    override fun setDefaultCurrency(currencyCode: String) = preferences.edit().putString(DEFAULT_CURRENCY, currencyCode).apply()
    override fun getDefaultCurrency(): String = preferences.getString(DEFAULT_CURRENCY, "USD")

    override fun setNightModeEnabled(enabled: Boolean) = preferences.edit().putBoolean(NIGHT_MODE, enabled).apply()
    override fun isNightModeEnabled(): Boolean = preferences.getBoolean(NIGHT_MODE, false)

    override fun setCoinListOrder(order: String) = preferences.edit().putString(COIN_LIST_ORDER, order).apply()
    override fun getCoinListOrder(): String = preferences.getString(COIN_LIST_ORDER, "coin_list_order_by_market_cap_DSC")

    override fun setCoinListDefaultChange(changeKey: String) = preferences.edit().putString(COIN_LIST_DEFAULT_CHANGE, changeKey).apply()
    override fun getCoinListDefaultChange(): String = preferences.getString(COIN_LIST_DEFAULT_CHANGE, "coin_list_change_24h")
}