package pl.karass32.xfolio.extension

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


fun BigDecimal.percentageDiff(secondValue: BigDecimal) : BigDecimal {
    return valueDiff(secondValue).divide(this, MathContext.DECIMAL32).multiply(BigDecimal("100")).setScale(2, RoundingMode.HALF_UP)
}

fun BigDecimal.valueDiff(secondValue: BigDecimal) : BigDecimal = secondValue.minus(this)