package pl.karass32.xfolio.repository.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import pl.karass32.xfolio.data.FiatRate

/**
 * Created by karas on 28.02.2018.
 */
@Dao
interface FiatRatesDao {

    @Query ("SELECT * FROM fiat_rates")
    fun getAllRates() : LiveData<List<FiatRate>>

    @Query ("SELECT code FROM fiat_rates")
    fun getAllFiatCodes() : LiveData<Array<String>>

    @Insert (onConflict = REPLACE)
    fun updateRates(rates: List<FiatRate>)
}