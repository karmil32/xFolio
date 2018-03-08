package pl.karass32.xfolio.repository.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
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