package pl.karass32.xfolio.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import pl.karass32.xfolio.data.GlobalCoinData

/**
 * Created by karas on 21.02.2018.
 */
@Dao
interface GlobalCoinDataDao {

    @Query("SELECT * FROM global_coin_data WHERE id = 0")
    fun getGlobalCoinData(): LiveData<GlobalCoinData>

    @Insert(onConflict = REPLACE)
    fun insertGlobalCoinData(globalCoinData: GlobalCoinData)

}