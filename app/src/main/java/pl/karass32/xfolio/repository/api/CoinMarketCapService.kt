package pl.karass32.xfolio.repository.api

import io.reactivex.Observable
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.data.GlobalCoinData
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by karas on 17.01.2018.
 */
interface CoinMarketCapService {

    @GET("global/")
    fun getGlobalCoinData() : Observable<GlobalCoinData>

    @GET("ticker/?limit=1500")
    fun getCoinList() : Observable<List<CoinData>>

    companion object {
        const val API_BASE_URL = "https://api.coinmarketcap.com/v1/"
        const val API_IMAGES_URL = "https://images.coinviewer.io/currencies/32x32/"

        fun create(): CoinMarketCapService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(API_BASE_URL)
                    .build()
            return retrofit.create(CoinMarketCapService::class.java)
        }
    }
}