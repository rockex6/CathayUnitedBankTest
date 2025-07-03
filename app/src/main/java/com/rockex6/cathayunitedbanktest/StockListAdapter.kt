package com.rockex6.cathayunitedbanktest

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rockex6.cathayunitedbanktest.databinding.ItemStockBinding
import com.rockex6.cathayunitedbanktest.model.StockTrading
import com.rockex6.cathayunitedbanktest.util.AnimationUtils

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

        private var currentStock: StockTrading? = null

        fun bind(stock: StockTrading) {
            val previousStock = currentStock
            currentStock = stock

            with(binding) {
                // 股票代號和名稱
                tvStockCode.text = "(${stock.code})"
                tvStockName.text = stock.name

                // 價格資訊 - 加入動畫效果
                updatePriceWithAnimation(tvOpenPrice, stock.openingPrice, previousStock?.openingPrice)
                updatePriceWithAnimation(tvClosePrice, stock.closingPrice, previousStock?.closingPrice)
                updatePriceWithAnimation(tvHighPrice, stock.highestPrice, previousStock?.highestPrice)
                updatePriceWithAnimation(tvLowPrice, stock.lowestPrice, previousStock?.lowestPrice)
                updatePriceWithAnimation(tvPriceChange, stock.change, previousStock?.change)

                // 交易資訊
                tvTradeCount.text = stock.transaction
                tvTradeVolume.text = stock.tradeVolume
                tvTradeValue.text = stock.tradeValue
                tvMonthlyAvgPrice.text = stock.monthlyAveragePrice

                // 設置價格顏色並加入動畫
                setPriceColorWithAnimation(tvOpenPrice, stock.openingPrice, stock.monthlyAveragePrice)
                setPriceColorWithAnimation(tvClosePrice, stock.closingPrice, stock.monthlyAveragePrice)

                // 漲跌價差顏色
                val changeColor = if (stock.change.startsWith("-")) {
                    ContextCompat.getColor(itemView.context, R.color.green)
                } else {
                    ContextCompat.getColor(itemView.context, R.color.red)
                }

                if (previousStock?.change != stock.change) {
                    animateColorChange(tvPriceChange, changeColor)
                } else {
                    tvPriceChange.setTextColor(changeColor)
                }

                // 點擊事件
                itemView.setOnClickListener {
                    AnimationUtils.clickFeedback(itemView)
                    itemClickCallback.invoke(stock.code)
                }
            }
        }

        private fun updatePriceWithAnimation(textView: android.widget.TextView, newValue: String, oldValue: String?) {
            textView.text = newValue

            // 如果價格有變化，加入動畫效果
            if (oldValue != null && oldValue != newValue) {
                // 縮放動畫
                AnimationUtils.scaleAnimation(textView, 1.0f, 1.2f, 150)

                // 延遲後縮放回原大小
                textView.postDelayed({
                    AnimationUtils.scaleAnimation(textView, 1.2f, 1.0f, 150)
                }, 150)
            }
        }

        private fun setPriceColorWithAnimation(textView: android.widget.TextView, price: String, avgPrice: String) {
            val newColor = if (price > avgPrice) {
                ContextCompat.getColor(itemView.context, R.color.red)
            } else {
                ContextCompat.getColor(itemView.context, R.color.green)
            }

            val currentColor = textView.currentTextColor
            if (currentColor != newColor) {
                animateColorChange(textView, newColor)
            } else {
                textView.setTextColor(newColor)
            }
        }

        private fun animateColorChange(textView: android.widget.TextView, toColor: Int) {
            val fromColor = textView.currentTextColor

            AnimationUtils.animateColorChange(
                textView,
                fromColor,
                toColor,
                300
            ) { color ->
                textView.setTextColor(color)
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