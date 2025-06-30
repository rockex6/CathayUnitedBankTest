package com.rockex6.cathayunitedbanktest

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rockex6.cathayunitedbanktest.databinding.ActivityMainBinding
import com.rockex6.cathayunitedbanktest.model.StockDetailInfo
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var stockAdapter: StockListAdapter
    private lateinit var viewModel: MainActivityViewModel
    private var loadingDialog: ProgressDialog? = null
    private var savedScrollPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initList()
        initLoadingDialog()
        observeData()
        initListener()

        // 載入股票資料
        viewModel.getAllStockData()
    }

    private fun initViewModel() {
        val factory = MainActivityViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]
    }

    private fun initList() {
        stockAdapter = StockListAdapter { code ->
            viewModel.getStockDetail(code)
        }

        binding.rvStock.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = stockAdapter
        }
    }

    private fun initLoadingDialog() {
        loadingDialog = ProgressDialog(this).apply {
            setMessage("載入股票資料中...")
            setCancelable(false)
        }
    }

    private fun observeData() {
        // 觀察載入狀態
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    loadingDialog?.show()
                } else {
                    loadingDialog?.dismiss()
                }
            }
        }

        // 觀察股票交易資料
        lifecycleScope.launch {
            viewModel.allStockList.collect { stockList ->
                // 使用 DiffUtil 自動計算差異並更新列表
                stockAdapter.submitList(stockList) {
                    // 在列表更新完成後恢復 scroll 位置
                    if (savedScrollPosition > -1) {
                        binding.rvStock.scrollToPosition(savedScrollPosition)
                        savedScrollPosition = -1 // 重置位置
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.stockDetailData.collect { stockDetail ->
                showStockDetailDialog(stockDetail)
            }
        }
    }

    private fun initListener() {
        binding.ivMenu.setOnClickListener {
            val bottomSheet = BottomSheet { sortEnum ->
                // 在排序前記錄當前的 scroll 位置
                saveCurrentScrollPosition()
                // 執行排序
                viewModel.changeSort(sortEnum)
            }
            bottomSheet.show(supportFragmentManager, BottomSheet::class.java.name)
        }
    }

    private fun saveCurrentScrollPosition() {
        val layoutManager = binding.rvStock.layoutManager as? LinearLayoutManager
        savedScrollPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
    }

    private fun showStockDetailDialog(stockData: StockDetailInfo?) {
        stockData?.let { stock ->
            val message = buildString {
                append("股票代號：${stock.code}\n")
                append("股票名稱：${stock.name}\n\n")
                append("本益比：${stock.peRatio}\n")
                append("殖利率：${stock.dividendYield}\n")
                append("股價淨值比：${stock.pbRatio}\n")
            }

            AlertDialog.Builder(this)
                .setTitle("股票詳細資訊")
                .setMessage(message)
                .setPositiveButton("確定") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.dismiss()
        loadingDialog = null
    }
}