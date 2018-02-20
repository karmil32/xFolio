package pl.karass32.xfolio.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import kotlinx.android.synthetic.main.coin_rv_layout.view.*
import pl.karass32.xfolio.R
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.extension.getColor
import pl.karass32.xfolio.repository.api.CoinMarketCapService
import pl.karass32.xfolio.repository.api.GlideApp
import pl.karass32.xfolio.util.NumberUtils
import java.math.BigDecimal


/**
 * Created by karas on 15.01.2018.
 */
class CoinRvAdapter(private var coinList: List<CoinData>) : RecyclerView.Adapter<CoinRvAdapter.ViewHolder>(), Filterable {

    private val mCoinListCopy by lazy {
        ArrayList<CoinData>(coinList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.coin_rv_layout, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(coinList[position])

    override fun getItemCount() = coinList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(coinData: CoinData) = with(itemView) {

            GlideApp.with(this)
                    .load("${CoinMarketCapService.API_IMAGES_URL}${coinData.id}.png")
                    .placeholder(null)
                    .into(coinRvLogo)

            coinData.price?.let {
                coinRvPrice.text = "$${NumberUtils.getPriceFormat(it).format(it)}"
            } ?: kotlin.run { coinRvPrice.text = "-" }

            coinData.change24h?.let {
                coinRvChange.text = "${NumberUtils.percentageFormat.format(it)}%"
                coinRvChange.setTextColor(if (it >= BigDecimal(0)) getColor(R.color.positiveColor) else getColor(R.color.negativeColor))
            } ?: kotlin.run {
                coinRvChange.text = "-"
                coinRvChange.setTextColor(getColor(R.color.negativeColor))
            }

            if (coinData.rank.toString().length > 2) { rankLayout.rotation = -45f } else { rankLayout.rotation = 0f }
            coinRvRankNumber.text = coinData.rank.toString()
            coinRvName.text = coinData.name
            coinRvSymbol.text = "(${coinData.symbol})"
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                if (charString.isEmpty()) {
                    coinList = mCoinListCopy
                } else {
                    val filteredList = ArrayList<CoinData>()
                    for (coinData in mCoinListCopy) {
                        if (coinData.name.toLowerCase().contains(charString.toLowerCase()) or coinData.symbol.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(coinData)
                        }
                    }

                    coinList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = coinList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                coinList = p1?.values as ArrayList<CoinData>
                notifyDataSetChanged()
            }
        }
    }
}