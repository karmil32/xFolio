package pl.karass32.xfolio.util

import org.joda.time.format.DateTimeFormat
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Created by karas on 08.02.2018.
 */

class NumberUtils {
    companion object {
        val percentageFormat = DecimalFormat("#0.00")
        val bigValueFormat = DecimalFormat("###,###.##")
        fun getPriceFormat(price: BigDecimal) = DecimalFormat(if (price > BigDecimal(1)) "###,###.00" else "#.######")

        val dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
    }
}