package pl.karass32.xfolio.util

/**
 * Created by karas on 13.02.2018.
 */
enum class CoinOrder(val value : String) {
    BY_MARKET_CAP_DSC("coin_list_order_by_market_cap_DSC"),
    BY_MARKET_CAP_ASC("coin_list_order_by_market_cap_ASC"),
    BY_PRICE_DSC("coin_list_order_by_price_DSC"),
    BY_PRICE_ASC("coin_list_order_by_price_ASC"),
    BY_CHANGE_1H_DSC("coin_list_order_by_change_1h_DSC"),
    BY_CHANGE_1H_ASC("coin_list_order_by_change_1h_ASC"),
    BY_CHANGE_24H_DSC("coin_list_order_by_change_24h_DSC"),
    BY_CHANGE_24H_ASC("coin_list_order_by_change_24h_ASC");

    companion object {
        fun of(value: String): CoinOrder = CoinOrder.values().first { it.value == value }
    }
}