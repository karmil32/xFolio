package pl.karass32.xfolio.util

import pl.karass32.xfolio.data.FiatCurrency
import java.math.BigDecimal
import java.util.*

/**
 * Created by karas on 27.02.2018.
 */
class CurrencyUtils {
    companion object {
        fun getFormattedPrice(usdPrice: BigDecimal, currencyCode: String) : String {
            val formattedStringValue = NumberUtils.getPriceFormat(usdPrice).format(usdPrice)

            return getValueWithSymbol(currencyCode, formattedStringValue)
        }

        fun getConvertedBigValue(usdPrice: BigDecimal, fiatRate: FiatCurrency, withSymbol: Boolean) : String {
            val convertedValue = usdPrice * fiatRate.currencyRate
            val formattedStringValue = NumberUtils.bigValueFormat.format(convertedValue)

            return if (withSymbol) getValueWithSymbol(fiatRate.currencyCode, formattedStringValue) else formattedStringValue
        }

        private fun getValueWithSymbol(currencyCode: String, value: String) =  getCurrencySymbol(currencyCode)+value

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