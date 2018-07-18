package pl.karass32.xfolio.util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class AxisValueFormatter(private val timePeriod: Long) : IAxisValueFormatter {

    private val monthYearFormat = SimpleDateFormat("MMM / YY")
    private val dayMonthFormat = SimpleDateFormat("dd MMM")
    private val hourDayFormat = SimpleDateFormat("EEE HH:mm")

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        val days = TimeUnit.SECONDS.toDays(timePeriod)

        return when {
            days < 3 -> hourDayFormat.format(value*1000)
            days < 100 -> dayMonthFormat.format(value*1000)
            else -> monthYearFormat.format(value*1000)
        }
    }
}