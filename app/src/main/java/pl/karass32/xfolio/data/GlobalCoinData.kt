package pl.karass32.xfolio.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by karas on 24.01.2018.
 */
@Entity(tableName = "global_coin_data")
data class GlobalCoinData(@PrimaryKey (autoGenerate = false) val id: Long = 0,
                          @ColumnInfo(name = "total_market_cap_usd") @SerializedName("total_market_cap_usd") var totalMarketCap: Long,
                          @ColumnInfo(name = "total_24h_volume_usd") @SerializedName("total_24h_volume_usd") var total24hVolume: Long,
                          @ColumnInfo(name = "bitcoin_percentage_of_market_cap") @SerializedName("bitcoin_percentage_of_market_cap") private val _bitcoinDominance: BigDecimal,
                          @ColumnInfo(name = "active_currencies") @SerializedName("active_currencies") val activeCurrencies: Int,
                          @ColumnInfo(name = "active_assets") @SerializedName("active_assets") val activeAssets: Int,
                          @ColumnInfo(name = "active_markets") @SerializedName("active_markets") val activeMarkets: Int,
                          @ColumnInfo(name = "last_updated") @SerializedName("last_updated") val lastUpdated: Long) {

    var bitcoinDominance: BigDecimal = _bitcoinDominance
        get() = _bitcoinDominance.setScale(2, RoundingMode.HALF_UP)
}