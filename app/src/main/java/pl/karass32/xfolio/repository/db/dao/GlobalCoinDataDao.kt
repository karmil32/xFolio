package pl.karass32.xfolio.repository.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
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