package pl.karass32.xfolio.repository.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorites_list")
data class FavoriteEntity(@PrimaryKey(autoGenerate = false) @ColumnInfo(name = "coin_name") val coinName: String)