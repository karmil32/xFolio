package pl.karass32.xfolio.repository.pref

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by karas on 08.03.2018.
 */
class SharedPreferencesRepositoryImpl(context: Context) : SharedPreferencesRepository {

    companion object {
        const val DEFAULT_CURRENCY = "DEFAULT_CURRENCY"
    }

    private val preferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    override fun setDefaultCurrency(currencyCode: String) = preferences.edit().putString(DEFAULT_CURRENCY, currencyCode).apply()
    override fun getDefaultCurrency(): String = preferences.getString(DEFAULT_CURRENCY, "USD")
}