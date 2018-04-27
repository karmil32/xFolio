package pl.karass32.xfolio.data

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CurrencyRatesResponse(
        @SerializedName("timestamp") val timestamp: Long,
        @SerializedName("rates") val rates: Map<String, BigDecimal>)