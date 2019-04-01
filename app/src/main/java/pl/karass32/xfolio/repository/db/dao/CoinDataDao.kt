package pl.karass32.xfolio.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import pl.karass32.xfolio.data.CoinData

/**
 * Created by karas on 20.02.2018.
 */
@Dao
interface CoinDataDao {

    @Query ("SELECT * FROM coin_data_list ORDER BY rank")
    fun getAll() : LiveData<List<CoinData>>

    @Query ("SELECT * FROM coin_data_list WHERE id in (:favIds)")
    fun getFavorites(favIds: List<String>) : LiveData<List<CoinData>>

    @Query("SELECT * FROM COIN_DATA_LIST WHERE symbol = (:coinSymbol)")
    fun getBySymbol(coinSymbol: String) : LiveData<CoinData>

    @Insert (onConflict = REPLACE)
    fun insert(list: List<CoinData>)
}