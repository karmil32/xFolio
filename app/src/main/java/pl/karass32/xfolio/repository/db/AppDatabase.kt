package pl.karass32.xfolio.repository.db

import android.arch.persistence.room.*
import pl.karass32.xfolio.data.CoinAlarm
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.data.FiatCurrency
import pl.karass32.xfolio.data.GlobalCoinData

/**
 * Created by karas on 20.02.2018.
 */
@Database(entities = [CoinData::class, GlobalCoinData::class, FiatCurrency::class, FavoriteEntity::class, CoinAlarm::class], version = 4)
@TypeConverters(DbTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun coinDataDao(): CoinDataDao
    abstract fun globalCoinDataDao(): GlobalCoinDataDao
    abstract fun fiatCurrencyDao(): FiatCurrencyDao
    abstract fun favNameDao() : FavoritesDao
    abstract fun alarmsDao() : AlarmsDao
}