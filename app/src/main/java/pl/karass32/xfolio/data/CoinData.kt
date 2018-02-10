package pl.karass32.xfolio.data

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Created by karas on 15.01.2018.
 */
data class CoinData(@SerializedName("id") val id: String,
                    @SerializedName("rank") val rank: Int,
                    @SerializedName("name") val name: String,
                    @SerializedName("symbol") val symbol: String,
                    @SerializedName("price_usd") val price: BigDecimal?,
                    @SerializedName("available_supply") val availableSupply: BigDecimal?,
                    @SerializedName("total_supply") val totalSupply: BigDecimal?,
                    @SerializedName("percent_change_7d") val change7d: BigDecimal?,
                    @SerializedName("percent_change_24h") val change24h: BigDecimal?,
                    @SerializedName("percent_change_1h") val change1h: BigDecimal?,
                    @SerializedName("24h_volume_usd") val volume24h: BigDecimal?,
                    @SerializedName("market_cap_usd") val marketCap: BigDecimal?,
                    @SerializedName("last_updated") val lastUpdated: Long?)