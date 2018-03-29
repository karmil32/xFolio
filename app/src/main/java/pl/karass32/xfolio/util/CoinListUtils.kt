package pl.karass32.xfolio.util

import pl.karass32.xfolio.data.CoinData

class CoinListUtils {

    companion object {
        fun sort(list: List<CoinData>?, orderMethod: CoinOrder) : ArrayList<CoinData> {
            val sortedList = when (orderMethod) {
                CoinOrder.BY_MARKET_CAP_DSC -> list?.sortedBy { it.rank }
                CoinOrder.BY_MARKET_CAP_ASC -> list?.sortedByDescending { it.rank }
                CoinOrder.BY_PRICE_DSC -> list?.sortedByDescending { it.price }
                CoinOrder.BY_PRICE_ASC -> list?.sortedBy { it.price }
                CoinOrder.BY_CHANGE_1H_DSC -> list?.sortedByDescending { it.change1h }
                CoinOrder.BY_CHANGE_1H_ASC -> list?.sortedBy { it.change1h }
                CoinOrder.BY_CHANGE_24H_DSC -> list?.sortedByDescending { it.change24h }
                CoinOrder.BY_CHANGE_24H_ASC -> list?.sortedBy { it.change24h }
            }
            return ArrayList(sortedList)
        }
    }
}