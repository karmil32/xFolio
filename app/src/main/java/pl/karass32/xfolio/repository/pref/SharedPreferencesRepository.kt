package pl.karass32.xfolio.repository.pref

/**
 * Created by karas on 08.03.2018.
 */
interface SharedPreferencesRepository {

    fun setLanguage(lang: String)
    fun getLanguage() : String

    fun setAutoOpen(fragmentTag: String)
    fun getAutoOpen() : String

    fun setDefaultCurrency(currencyCode: String)
    fun getDefaultCurrency() : String

    fun setCoinListOrder(order: String)
    fun getCoinListOrder() : String

    fun setCoinListDefaultChange(changeKey: String)
    fun getCoinListDefaultChange() : String
}