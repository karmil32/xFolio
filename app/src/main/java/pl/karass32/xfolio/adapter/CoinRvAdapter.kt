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
class CoinRvAdapter(private var coinList: ArrayList<CoinData>) : RecyclerView.Adapter<CoinRvAdapter.ViewHolder>(), Filterable {

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

            coinRvRankNumber.text = coinData.rank.toString()
            coinRvName.text = coinData.name
            coinRvSymbol.text = "(${coinData.symbol})"
            coinRvPrice.text = "$${NumberUtils.getPriceFormat(coinData.price).format(coinData.price)}"
            coinRvChange.text = "${NumberUtils.percentageFormat.format(coinData.change24h)}%"

            coinRvChange.setTextColor(if (coinData.change24h >= BigDecimal(0)) getColor(R.color.positiveColor) else getColor(R.color.negativeColor))
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
                        if (coinData.name.toLowerCase().contains(charString.toLowerCase())) {
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