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
data class FiatRate(@PrimaryKey(autoGenerate = false) @ColumnInfo(name = "code") @SerializedName("code") val currencyCode: String,
                    @ColumnInfo(name = "rate") @SerializedName("rate") val currencyRate: BigDecimal,
                    val lastUpdate: Long)