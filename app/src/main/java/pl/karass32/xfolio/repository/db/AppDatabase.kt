package pl.karass32.xfolio.repository.db

import android.arch.persistence.room.*
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.data.FiatRate
import pl.karass32.xfolio.data.GlobalCoinData

/**
 * Created by karas on 20.02.2018.
 */
@Database(entities = [CoinData::class, GlobalCoinData::class, FiatRate::class], version = 2)
@TypeConverters(DbTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun coinDataDao(): CoinDataDao
    abstract fun globalCoinDataDao(): GlobalCoinDataDao
    abstract fun fiatRatesDao(): FiatRatesDao
}