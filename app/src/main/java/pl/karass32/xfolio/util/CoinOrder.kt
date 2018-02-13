package pl.karass32.xfolio.util

/**
 * Created by karas on 13.02.2018.
 */
enum class CoinOrder(val value : Int) {
    BY_MARKET_CAP_DSC(10),
    BY_MARKET_CAP_ASC(11),
    BY_PRICE_DSC(20),
    BY_PRICE_ASC(21),
    BY_CHANGE_1H_DSC(30),
    BY_CHANGE_1H_ASC(31),
    BY_CHANGE_24H_DSC(40),
    BY_CHANGE_24H_ASC(41)
}