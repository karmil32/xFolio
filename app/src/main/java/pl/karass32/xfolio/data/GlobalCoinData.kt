package pl.karass32.xfolio.data

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Created by karas on 24.01.2018.
 */
data class GlobalCoinData(@SerializedName("total_market_cap_usd") val totalMarketCap: BigDecimal,
                          @SerializedName("total_24h_volume_usd") val total24hVolume: BigDecimal,
                          @SerializedName("bitcoin_percentage_of_market_cap") val bitcoinDominance: BigDecimal,
                          @SerializedName("active_currencies") val activeCurrencies: Int,
                          @SerializedName("active_assets") val activeAssets: Int,
                          @SerializedName("active_markets") val activeMarkets: Int,
                          @SerializedName("last_updated") val lastUpdated: Long)