package pl.karass32.xfolio.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Created by karas on 27.02.2018.
 */

@Entity(tableName = "fiat_rates")
data class FiatCurrency(@PrimaryKey(autoGenerate = false) @ColumnInfo(name = "code") val code: String,
                        @ColumnInfo(name = "rate") val rate: BigDecimal,
                        @ColumnInfo(name = "timestamp") val timestamp: Long)