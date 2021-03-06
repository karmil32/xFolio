package pl.karass32.xfolio.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by karas on 15.01.2018.
 */
@Entity(tableName = "coin_data_list")
data class CoinData(@PrimaryKey(autoGenerate = false) @SerializedName("id") val id: String,
                    @ColumnInfo(name = "rank") @SerializedName("rank") val rank: Int,
                    @ColumnInfo(name = "name") @SerializedName("name") val name: String,
                    @ColumnInfo(name = "symbol") @SerializedName("symbol") val symbol: String,
                    @ColumnInfo(name = "price_usd") @SerializedName("price_usd") private var _price: BigDecimal?,
                    @ColumnInfo(name = "available_supply") @SerializedName("available_supply") val availableSupply: Long?,
                    @ColumnInfo(name = "total_supply") @SerializedName("total_supply") val totalSupply: Long?,
                    @ColumnInfo(name = "percent_change_7d") @SerializedName("percent_change_7d") private val _change7d: BigDecimal?,
                    @ColumnInfo(name = "percent_change_24h") @SerializedName("percent_change_24h") private val _change24h: BigDecimal?,
                    @ColumnInfo(name = "percent_change_1h") @SerializedName("percent_change_1h") private val _change1h: BigDecimal?,
                    @ColumnInfo(name = "24h_volume_usd") @SerializedName("24h_volume_usd") var volume24h: BigDecimal?,
                    @ColumnInfo(name = "market_cap_usd") @SerializedName("market_cap_usd") var marketCap: BigDecimal?,
                    @ColumnInfo(name = "last_updated") @SerializedName("last_updated") val lastUpdated: Long?) {

    var price: BigDecimal?
        get() = _price?.let { if (it > BigDecimal("1")) it.setScale(2, RoundingMode.HALF_UP) else it.setScale(7, RoundingMode.HALF_UP) }
        set(value) {
            _price = value
        }

    var change7d: BigDecimal? = null
        get() = _change7d?.setScale(2, RoundingMode.HALF_UP)

    var change24h: BigDecimal? = null
        get() = _change24h?.setScale(2, RoundingMode.HALF_UP)

    var change1h: BigDecimal? = null
        get() = _change1h?.setScale(2, RoundingMode.HALF_UP)

}

