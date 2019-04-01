package pl.karass32.xfolio.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import pl.karass32.xfolio.data.FiatCurrency

/**
 * Created by karas on 28.02.2018.
 */
@Dao
interface FiatCurrencyDao {

    @Query ("SELECT * FROM fiat_rates")
    fun getAllRates() : LiveData<List<FiatCurrency>>

    @Query ("SELECT code FROM fiat_rates")
    fun getAllFiatCodes() : LiveData<Array<String>>

    @Query ("SELECT * FROM fiat_rates WHERE code LIKE :fiatCode LIMIT 1")
    fun getCurrency(fiatCode: String) : FiatCurrency

    @Insert (onConflict = REPLACE)
    fun updateRates(rates: List<FiatCurrency>)
}