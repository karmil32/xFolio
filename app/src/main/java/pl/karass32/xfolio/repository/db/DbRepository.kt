package pl.karass32.xfolio.repository.db

import pl.karass32.xfolio.data.CoinData

/**
 * Created by karas on 17.01.2018.
 */
interface DbRepository {
    fun getCoinList() : ArrayList<CoinData>
}