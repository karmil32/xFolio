package pl.karass32.xfolio.repository.pref

/**
 * Created by karas on 08.03.2018.
 */
interface SharedPreferencesRepository {

    fun setDefaultCurrency(currencyCode: String)
    fun getDefaultCurrency() : String
}