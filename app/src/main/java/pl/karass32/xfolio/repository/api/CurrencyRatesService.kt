package pl.karass32.xfolio.repository.api

import io.reactivex.Observable
import pl.karass32.xfolio.data.CurrencyRatesResponse
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CurrencyRatesService {

    @GET("api")
    fun getLatestRates() : Observable<CurrencyRatesResponse>

    companion object {
        private const val API_BASE_URL = "http://www.cypresshill.org/"

        fun create(): CurrencyRatesService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(API_BASE_URL)
                    .build()
            return retrofit.create(CurrencyRatesService::class.java)
        }
    }
}