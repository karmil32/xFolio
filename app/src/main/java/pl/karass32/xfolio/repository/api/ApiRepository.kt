package pl.karass32.xfolio.repository.api

import io.reactivex.Observable
import pl.karass32.xfolio.data.CoinData

/**
 * Created by karas on 17.01.2018.
 */
interface ApiRepository {
    fun getCoinList() : Observable<ArrayList<CoinData>>
}