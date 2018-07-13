package pl.karass32.xfolio.data

import com.google.gson.annotations.SerializedName

data class HistDataEntry(
        @SerializedName("time") val time: Long,
        @SerializedName("close") val close: String,
        @SerializedName("high") val high: String,
        @SerializedName("low") val low: String,
        @SerializedName("open") val open: String,
        @SerializedName("volumefrom") val volumefrom: String,
        @SerializedName("volumeto") val volumeto: String
)