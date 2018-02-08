package pl.karass32.xfolio.util

import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Created by karas on 08.02.2018.
 */

class NumberUtils {
    companion object {
        val percentageFormat = DecimalFormat("#0.00")
        fun getPriceFormat(price: BigDecimal) = DecimalFormat(if (price > BigDecimal(1)) "###,###.00" else "#.####")
    }
}