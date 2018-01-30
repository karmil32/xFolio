package pl.karass32.xfolio.data

import java.math.BigDecimal

/**
 * Created by karas on 24.01.2018.
 */
data class GlobalCoinData(val totalMarketCap: BigDecimal,
                          val total24hVolume: BigDecimal,
                          val bitcoinDominance: BigDecimal,
                          val activeCurrencies: Int,
                          val activeAssets: Int,
                          val activeMarkets: Int,
                          val lastUpdated: Long)