package pl.karass32.xfolio.repository.db

import android.arch.lifecycle.LiveData
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
    fun getAll() : LiveData<List<CoinData>>

    @Query ("SELECT * FROM coin_data_list WHERE id in (:favIds)")
    fun getFavorites(favIds: List<String>) : LiveData<List<CoinData>>

    @Query("SELECT * FROM COIN_DATA_LIST WHERE id = (:id)")
    fun getById(id: String) : LiveData<CoinData>

    @Insert (onConflict = REPLACE)
    fun insert(list: List<CoinData>)
}