package pl.karass32.xfolio.repository.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
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