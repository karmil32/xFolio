package pl.karass32.xfolio.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import pl.karass32.xfolio.base.BaseCoinListViewModel
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.util.CoinListUtils
import pl.karass32.xfolio.util.CoinOrder

class FavoritesListViewModel : BaseCoinListViewModel() {

    private val map = Transformations.switchMap(currency) { _ -> appDb.favNameDao().getAll() }

    override fun getCoinList(): LiveData<List<CoinData>>? {
        if (coinListMediator == null) {
            coinListMediator = MediatorLiveData()
            coinListMediator?.addSource(Transformations.switchMap(map) { favIds ->
                return@switchMap appDb.coinDataDao().getFavorites(favIds)
            }) { list ->
                if (preferences.getDefaultCurrency() != "USD") {
                    list?.forEach { coin ->
                        coin.price = coin.price?.multiply(currency.value?.rate)
                        coin.volume24h = coin.volume24h?.multiply(currency.value?.rate)
                        coin.marketCap = coin.marketCap?.multiply(currency.value?.rate)
                    }
                }
                if (preferences.getCoinListOrder() != CoinOrder.BY_MARKET_CAP_DSC.value) {
                    coinListMediator?.value = CoinListUtils.sort(list, CoinOrder.of(preferences.getCoinListOrder()))
                } else {
                    coinListMediator?.value = list
                }
            }
        }
        return coinListMediator
    }

    override fun onFavToggleClicked(id: String) {
        appDb.favNameDao().delete(id)
    }
}