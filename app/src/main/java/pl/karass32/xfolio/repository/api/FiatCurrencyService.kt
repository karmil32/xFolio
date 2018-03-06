package pl.karass32.xfolio.repository.api

import io.reactivex.Observable
import pl.karass32.xfolio.data.FiatCurrency
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by karas on 28.02.2018.
 */
interface FiatCurrencyService {

    @GET("fiat_rates.json")
    fun getLatestRates() : Observable<List<FiatCurrency>>

    companion object {
        private const val API_BASE_URL = "http://cypresshill.org/mobileAPI/"

        fun create(): FiatCurrencyService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(API_BASE_URL)
                    .build()
            return retrofit.create(FiatCurrencyService::class.java)
        }
    }
}