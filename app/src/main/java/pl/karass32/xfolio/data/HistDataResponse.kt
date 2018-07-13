package pl.karass32.xfolio.data

import com.google.gson.annotations.SerializedName

data class HistDataResponse(
        @SerializedName("Response") val response: String,
        @SerializedName("Type") val type: Int,
        @SerializedName("Aggregated") val aggregated: Boolean,
        @SerializedName("Data") val data: List<HistDataEntry>,
        @SerializedName("TimeTo") val timeTo: Long,
        @SerializedName("TimeFrom") val timeFrom: Long,
        @SerializedName("FirstValueInArray") val firstValueInArray: Boolean
)