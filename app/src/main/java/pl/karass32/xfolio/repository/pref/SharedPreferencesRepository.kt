package pl.karass32.xfolio.repository.pref

/**
 * Created by karas on 08.03.2018.
 */
interface SharedPreferencesRepository {

    fun setDefaultCurrency(currencyCodeKey: String)
    fun getDefaultCurrency() : String

    fun setCoinListOrder(orderKey: String)
    fun getCoinListOrder() : String

    fun setCoinListDefaultChange(changeKey: String)
    fun getCoinListDefaultChange() : String
}