package pl.karass32.xfolio.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import pl.karass32.xfolio.data.CoinAlarm

@Dao
interface AlarmsDao {

    @Query("SELECT * FROM coin_alarms")
    fun getAll() : LiveData<List<CoinAlarm>>

    @Query ("SELECT * FROM coin_alarms WHERE is_enabled = 1")
    fun getEnabled() : LiveData<List<CoinAlarm>>

    @Query("SELECT * FROM coin_alarms WHERE coin_symbol = (:coinSymbol)")
    fun getBySymbol(coinSymbol: String) : LiveData<List<CoinAlarm>>

    @Insert(onConflict = REPLACE)
    fun insert(alarm: CoinAlarm)
}