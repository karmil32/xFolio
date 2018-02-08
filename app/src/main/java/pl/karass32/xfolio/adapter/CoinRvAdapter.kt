package pl.karass32.xfolio.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.coin_rv_layout.view.*
import pl.karass32.xfolio.R
import pl.karass32.xfolio.data.CoinData
import pl.karass32.xfolio.extension.getColor
import pl.karass32.xfolio.repository.api.CoinMarketCapService
import pl.karass32.xfolio.repository.api.GlideApp
import java.math.BigDecimal
import java.text.DecimalFormat


/**
 * Created by karas on 15.01.2018.
 */
class CoinRvAdapter(private val coins: ArrayList<CoinData>) : RecyclerView.Adapter<CoinRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.coin_rv_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(coins[position])

    override fun getItemCount(): Int {
        return coins.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(coinData: CoinData) = with(itemView) {

            val pricePattern = if (coinData.price > BigDecimal(1)) "###,###.00" else "#.####"
            val priceFormat = DecimalFormat(pricePattern)

            val percentagePattern = "#0.00"
            val percentageFormat = DecimalFormat(percentagePattern)

            GlideApp.with(this)
                    .load("${CoinMarketCapService.API_IMAGES_URL}${coinData.id}.png")
                    .placeholder(null)
                    .into(coinRvLogo)

            coinRvRankNumber.text = coinData.rank.toString()
            coinRvName.text = coinData.name
            coinRvSymbol.text = "(${coinData.symbol})"
            coinRvPrice.text = "$${priceFormat.format(coinData.price)}"
            coinRvChange.text = "${percentageFormat.format(coinData.change24h)}%"

            coinRvChange.setTextColor(if (coinData.change24h >= BigDecimal(0)) getColor(R.color.positiveColor) else getColor(R.color.negativeColor))
        }
    }
}