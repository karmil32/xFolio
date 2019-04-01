package pl.karass32.xfolio.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import pl.karass32.xfolio.repository.db.FavoriteEntity

@Dao
interface FavoritesDao {

    @Query("SELECT coin_name FROM favorites_list")
    fun getAll(): LiveData<List<String>>

    @Insert(onConflict = IGNORE)
    fun insert(name: FavoriteEntity)

    @Query("DELETE FROM favorites_list WHERE coin_name = :name")
    fun delete(name: String)

    @Query("SELECT COUNT(*) FROM favorites_list WHERE coin_name = :name LIMIT 1")
    fun isAdded(name: String): Boolean
}