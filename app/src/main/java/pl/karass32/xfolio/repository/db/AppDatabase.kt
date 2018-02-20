package pl.karass32.xfolio.repository.db

import android.arch.persistence.room.*
import pl.karass32.xfolio.data.CoinData

/**
 * Created by karas on 20.02.2018.
 */
@Database(entities = [CoinData::class], version = 1)
@TypeConverters(DbTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun coinDataDao(): CoinDataDao
}