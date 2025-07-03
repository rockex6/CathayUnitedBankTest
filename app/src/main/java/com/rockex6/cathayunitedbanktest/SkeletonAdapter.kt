package com.rockex6.cathayunitedbanktest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rockex6.cathayunitedbanktest.databinding.ItemStockSkeletonBinding

/**
 * 骨架屏適配器
 * 用於在載入資料時顯示骨架屏效果
 */
class SkeletonAdapter(private val itemCount: Int = 5) : 
    RecyclerView.Adapter<SkeletonAdapter.SkeletonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkeletonViewHolder {
        val binding = ItemStockSkeletonBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return SkeletonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SkeletonViewHolder, position: Int) {
        // 骨架屏不需要綁定資料
    }

    override fun getItemCount(): Int = itemCount

    class SkeletonViewHolder(binding: ItemStockSkeletonBinding) : 
        RecyclerView.ViewHolder(binding.root)
}
