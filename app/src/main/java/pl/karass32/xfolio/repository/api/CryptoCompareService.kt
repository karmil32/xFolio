package pl.karass32.xfolio.repository.api

import io.reactivex.Observable
import pl.karass32.xfolio.data.HistDataResponse
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoCompareService {

    @GET("histoday?tsym=USD&allData=true&aggregate=1&e=CCCAGG")
    fun getAllHistoricalData(@Query("fsym") id: String?) : Observable<HistDataResponse>

    @GET("histoday?tsym=USD&limit=365&aggregate=1&e=CCCAGG")
    fun getLastYearHistoricalData(@Query("fsym") id: String?) : Observable<HistDataResponse>

    @GET("histoday?tsym=USD&limit=182&aggregate=1&e=CCCAGG")
    fun getLastSixMonthsHistoricalData(@Query("fsym") id: String?) : Observable<HistDataResponse>

    @GET("histohour?tsym=USD&limit=740&aggregate=1&e=CCCAGG")
    fun getLastMonthHistoricalData(@Query("fsym") id: String?) : Observable<HistDataResponse>

    @GET("histohour?tsym=USD&limit=168&aggregate=1&e=CCCAGG")
    fun getLastWeekHistoricalData(@Query("fsym") id: String?) : Observable<HistDataResponse>

    @GET("histominute?tsym=USD&limit=1440&aggregate=1&e=CCCAGG")
    fun getLastDayHistoricalData(@Query("fsym") id: String?) : Observable<HistDataResponse>

    companion object {
        const val API_BASE_URL = "https://min-api.cryptocompare.com/data/"

        fun create(): CryptoCompareService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(API_BASE_URL)
                    .build()
            return retrofit.create(CryptoCompareService::class.java)
        }
    }
}