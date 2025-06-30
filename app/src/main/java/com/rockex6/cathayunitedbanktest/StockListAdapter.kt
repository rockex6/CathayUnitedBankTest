package com.rockex6.cathayunitedbanktest

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rockex6.cathayunitedbanktest.databinding.ItemStockBinding
import com.rockex6.cathayunitedbanktest.model.StockTrading

class StockListAdapter(private val itemClickCallback: (String) -> Unit) :
    ListAdapter<StockTrading, StockListAdapter.StockViewHolder>(StockDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class StockViewHolder(private val binding: ItemStockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stock: StockTrading) {
            with(binding) {
                // 股票代號和名稱
                tvStockCode.text = "(${stock.code})"
                tvStockName.text = stock.name

                // 價格資訊
                tvOpenPrice.text = stock.openingPrice
                tvClosePrice.text = stock.closingPrice
                tvHighPrice.text = stock.highestPrice
                tvLowPrice.text = stock.lowestPrice
                tvPriceChange.text = stock.change

                // 交易資訊
                tvTradeCount.text = stock.transaction
                tvTradeVolume.text = stock.tradeVolume
                tvTradeValue.text = stock.tradeValue

                tvMonthlyAvgPrice.text = stock.monthlyAveragePrice

                if (stock.openingPrice > stock.monthlyAveragePrice) {
                    tvOpenPrice.setTextColor(ColorStateList.valueOf(itemView.context.getColor(R.color.green)))
                } else {
                    tvOpenPrice.setTextColor(ColorStateList.valueOf(itemView.context.getColor(R.color.red)))
                }

                if (stock.closingPrice > stock.monthlyAveragePrice) {
                    tvClosePrice.setTextColor(ColorStateList.valueOf(itemView.context.getColor(R.color.green)))
                } else {
                    tvClosePrice.setTextColor(ColorStateList.valueOf(itemView.context.getColor(R.color.red)))
                }

                if (stock.change.startsWith("-")) {
                    tvPriceChange.setTextColor(ColorStateList.valueOf(itemView.context.getColor(R.color.green)))
                } else {
                    tvPriceChange.setTextColor(ColorStateList.valueOf(itemView.context.getColor(R.color.red)))
                }
                itemView.setOnClickListener {
                    itemClickCallback.invoke(stock.code)
                }
            }
        }
    }
}

/**
 * DiffUtil.ItemCallback 用於比較 StockTrading 物件
 * 提供高效的列表更新機制
 */
class StockDiffCallback : DiffUtil.ItemCallback<StockTrading>() {

    override fun areItemsTheSame(oldItem: StockTrading, newItem: StockTrading): Boolean {
        // 使用股票代號作為唯一識別符
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: StockTrading, newItem: StockTrading): Boolean {
        // 比較所有相關欄位是否相同
        return oldItem == newItem
    }
}