package pl.karass32.xfolio.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.chauthai.swipereveallayout.ViewBinderHelper
import kotlinx.android.synthetic.main.coin_rv_layout.view.*
import kotlinx.android.synthetic.main.coin_rv_swipe_layout.view.*
import kotlinx.android.synthetic.main.coin_rv_swipe_menu_layout.view.*
import pl.karass32.xfolio.MyApplication
import pl.karass32.xfolio.R
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.extension.getColor
import pl.karass32.xfolio.repository.api.CoinMarketCapService
import pl.karass32.xfolio.repository.api.GlideApp
import pl.karass32.xfolio.repository.pref.SharedPreferencesRepository
import pl.karass32.xfolio.util.CurrencyUtils
import pl.karass32.xfolio.util.enum.ChangeOption
import java.math.BigDecimal
import javax.inject.Inject


/**
 * Created by karas on 15.01.2018.
 */
class CoinRvAdapter(private var onItemClickListener: OnItemClickListener, private var onSwipeMenuListener: OnSwipeMenuListener) : ListAdapter<CoinData, CoinRvAdapter.ViewHolder>(CoinDataDiffCallback()), Filterable {

    @Inject
    lateinit var preferences: SharedPreferencesRepository

    private val viewBinderHelper = ViewBinderHelper()

    private var mCoinListCopy: List<CoinData> = ArrayList()

    init {
        MyApplication.component.inject(this)
        viewBinderHelper.setOpenOnlyOne(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.coin_rv_swipe_layout, parent, false), onItemClickListener, onSwipeMenuListener, viewBinderHelper)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), preferences)

    class ViewHolder(itemView: View, onItemClickListener: OnItemClickListener, swipeMenuListener: OnSwipeMenuListener, private val viewBinderHelper: ViewBinderHelper) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        private val itemClickListener: OnItemClickListener = onItemClickListener
        private val menuListener: OnSwipeMenuListener = swipeMenuListener

        @SuppressLint("SetTextI18n")
        fun bind(coinData: CoinData, preferences: SharedPreferencesRepository) = with(itemView) {
            var isFavorite = menuListener.isFavorite(coinData.id)

            viewBinderHelper.bind(swipeRevealLayout, coinData.id)

            favToggleButton.setImageResource(if (isFavorite) R.drawable.ic_favorite_red_24dp else R.drawable.ic_favorite_border_red_24dp)

            favToggleButton.setOnClickListener {
                favToggleButton.setImageResource(if (isFavorite) R.drawable.ic_favorite_border_red_24dp else R.drawable.ic_favorite_red_24dp)
                isFavorite = !isFavorite
                viewBinderHelper.closeLayout(coinData.id)
                menuListener.onFavToggleClicked(coinData.id)
            }

            frontLayout.setOnClickListener { itemClickListener.onItemClick(coinData.symbol) }

            GlideApp.with(this)
                    .load("${CoinMarketCapService.API_IMAGES_URL}${coinData.id}.png")
                    .placeholder(null)
                    .into(coinRvLogo)

            coinData.price?.let {
                coinRvPrice.text = CurrencyUtils.getFormattedPrice(it, preferences.getDefaultCurrency())
            } ?: kotlin.run { coinRvPrice.text = "-" }

            val changeType = ChangeOption.of(preferences.getCoinListDefaultChange())
            val change = when (changeType) {
                ChangeOption.CHANGE_1H -> coinData.change1h
                ChangeOption.CHANGE_24H -> coinData.change24h
                ChangeOption.CHANGE_7D -> coinData.change7d
            }

            change?.let {
                coinRvChange.text = context.getString(R.string.percent_template, it)
                coinRvChange.setTextColor(if (it >= BigDecimal(0)) getColor(R.color.positiveColor) else getColor(R.color.negativeColor))
            } ?: kotlin.run {
                coinRvChange.text = "-"
                coinRvChange.setTextColor(getColor(R.color.negativeColor))
            }

            if (coinData.rank.toString().length > 2) rankLayout.rotation = -45f else rankLayout.rotation = 0f
            coinRvRankNumber.text = coinData.rank.toString()
            coinRvName.text = coinData.name
            coinRvSymbol.text = "(${coinData.symbol})"
        }
    }

    override fun submitList(list: List<CoinData>?) {
        super.submitList(list)
        if (mCoinListCopy.isEmpty()) {
            list?.let { mCoinListCopy = list }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val coinList: List<CoinData>
                val charString = p0.toString()
                if (charString.isEmpty()) {
                    coinList = mCoinListCopy
                } else {
                    val filteredList = ArrayList<CoinData>()
                    mCoinListCopy.filterTo(filteredList) { it.name.toLowerCase().contains(charString.toLowerCase()) or it.symbol.toLowerCase().contains(charString.toLowerCase()) }
                    coinList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = coinList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                val result = p1?.values as ArrayList<CoinData>
                submitList(result) //TODO Błąd z pozycją listy po usunięciu filtrowania.
            }
        }
    }

    class CoinDataDiffCallback : DiffUtil.ItemCallback<CoinData>() {
        override fun areItemsTheSame(oldItem: CoinData, newItem: CoinData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CoinData, newItem: CoinData): Boolean {
            return oldItem == newItem
        }
    }

    interface OnSwipeMenuListener {
        fun onFavToggleClicked(id: String)
        fun isFavorite(id: String) : Boolean
    }

    interface OnItemClickListener {
        fun onItemClick(coinSymbol: String)
    }
}