package pl.karass32.xfolio.repository.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query

@Dao
interface FavoritesDao {

    @Query("SELECT coin_name FROM favorites_list")
    fun getAll(): List<String>

    @Insert(onConflict = IGNORE)
    fun insert(name: FavoriteEntity)

    @Query("DELETE FROM favorites_list WHERE coin_name = :name")
    fun delete(name: String)

    @Query("SELECT COUNT(*) FROM favorites_list WHERE coin_name = :name LIMIT 1")
    fun isAdded(name: String): Boolean
}