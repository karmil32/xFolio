package pl.karass32.xfolio.util.enum

/**
 * Created by karas on 26.02.2018.
 */
enum class ChangeOption(val value: String) {
    CHANGE_1H("coin_list_change_1h"),
    CHANGE_24H("coin_list_change_24h"),
    CHANGE_7D("coin_list_change_7d");

    companion object {
        fun of(value: String): ChangeOption = ChangeOption.values().first { it.value == value }
    }
}