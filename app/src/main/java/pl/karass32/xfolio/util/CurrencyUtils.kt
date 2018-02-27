package pl.karass32.xfolio.util

import java.util.*

/**
 * Created by karas on 27.02.2018.
 */
class CurrencyUtils {
    companion object {
        fun getFormattedValue(currencyCode: String, value: Int) =  getCurrencySymbol(currencyCode)+value.toString()

        private fun getCurrencySymbol(currencyCode: String) : String {
            val currency = Currency.getInstance(currencyCode)
            return currency.getSymbol(currencyLocaleMap[currency])
        }

        private val currencyLocaleMap: SortedMap<Currency, Locale> by lazy {
            getCurrencyLocaleMap()
        }

        private fun getCurrencyLocaleMap(): TreeMap<Currency, Locale> {
            val map = TreeMap<Currency, Locale>(Comparator<Currency> { p0, p1 ->
                p0.currencyCode.compareTo(p1.currencyCode) })

            for (locale in Locale.getAvailableLocales()) {
                try {
                    val currency = Currency.getInstance(locale)
                    map[currency] = locale
                } catch (e: Exception) {}
            }
            return map
        }
    }
}