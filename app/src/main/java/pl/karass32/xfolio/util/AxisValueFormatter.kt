package pl.karass32.xfolio.util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat

class AxisValueFormatter : IAxisValueFormatter {

    private val simpleDateFormat = SimpleDateFormat("MMM / YY")

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return simpleDateFormat.format(value*1000)
    }
}