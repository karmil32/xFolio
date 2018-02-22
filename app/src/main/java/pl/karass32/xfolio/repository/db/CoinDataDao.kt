package pl.karass32.xfolio.repository.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import pl.karass32.xfolio.data.CoinData

/**
 * Created by karas on 20.02.2018.
 */
@Dao
interface CoinDataDao {

    @Query ("SELECT * FROM coin_data_list ORDER BY rank")
    fun getAllCoinData() : List<CoinData>

    @Insert (onConflict = REPLACE)
    fun insertCoinData(list: List<CoinData>)
}