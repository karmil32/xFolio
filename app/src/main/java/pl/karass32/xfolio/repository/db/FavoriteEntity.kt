package pl.karass32.xfolio.repository.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "favorites_list")
data class FavoriteEntity(@PrimaryKey(autoGenerate = false) @ColumnInfo(name = "coin_name") val coinName: String)