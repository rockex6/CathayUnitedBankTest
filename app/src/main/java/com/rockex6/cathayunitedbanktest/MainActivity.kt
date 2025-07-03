package com.rockex6.cathayunitedbanktest

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import com.rockex6.cathayunitedbanktest.databinding.ActivityMainBinding
import com.rockex6.cathayunitedbanktest.util.AnimationUtils
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var stockAdapter: StockListAdapter
    private lateinit var skeletonAdapter: SkeletonAdapter
    private lateinit var viewModel: MainActivityViewModel
    private var savedScrollPosition: Int = -1
    private var isFirstLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initList()
        initSkeletonList()
        observeData()
        initListener()
        setupActivityAnimations()

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
            // 加入項目動畫
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
                removeDuration = 300
            }
        }
    }

    private fun initSkeletonList() {
        skeletonAdapter = SkeletonAdapter(5)

        binding.rvSkeleton.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = skeletonAdapter
        }
    }

    private fun setupActivityAnimations() {
        // 設置 Activity 轉場動畫
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )

        // 初始化時隱藏主要內容
        binding.rvStock.alpha = 0f
        binding.ivMenu.alpha = 0f
    }

    private fun observeData() {
        // 觀察載入狀態
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                handleLoadingState(isLoading)
            }
        }

        // 觀察股票交易資料
        lifecycleScope.launch {
            viewModel.allStockList.collect { stockList ->
                handleStockListUpdate(stockList)
            }
        }

        lifecycleScope.launch {
            viewModel.stockDetailData.collect { message ->
                showStockDetailDialog(message)
            }
        }
    }

    private fun initListener() {
        binding.ivMenu.setOnClickListener {
            // 加入點擊反饋動畫
            AnimationUtils.clickFeedback(it)

            val bottomSheet = BottomSheet.newInstance { sortEnum ->
                // 在排序前記錄當前的 scroll 位置
                saveCurrentScrollPosition()
                // 執行排序
                viewModel.changeSort(sortEnum)
            }
            bottomSheet.show(supportFragmentManager, BottomSheet::class.java.name)
        }
    }

    private fun handleLoadingState(isLoading: Boolean) {
        if (isLoading) {
            if (isFirstLoad) {
                // 第一次載入顯示自定義載入動畫
                binding.loadingContainer.visibility = View.VISIBLE
                binding.rvSkeleton.visibility = View.GONE
                binding.rvStock.visibility = View.GONE
            } else {
                // 後續載入顯示骨架屏
                binding.loadingContainer.visibility = View.GONE
                binding.rvSkeleton.visibility = View.VISIBLE
                binding.rvStock.visibility = View.GONE

                // 骨架屏淡入動畫
                AnimationUtils.fadeIn(binding.rvSkeleton, 200)
            }
        } else {
            // 載入完成，隱藏載入動畫
            if (binding.loadingContainer.visibility == View.VISIBLE) {
                AnimationUtils.fadeOut(binding.loadingContainer, 300)
            }
            if (binding.rvSkeleton.visibility == View.VISIBLE) {
                AnimationUtils.fadeOut(binding.rvSkeleton, 200)
            }
        }
    }

    private fun handleStockListUpdate(stockList: List<com.rockex6.cathayunitedbanktest.model.StockTrading>) {
        stockAdapter.submitList(stockList) {
            // 顯示股票列表
            if (binding.rvStock.visibility != View.VISIBLE) {
                binding.rvStock.visibility = View.VISIBLE

                if (isFirstLoad) {
                    // 第一次載入的動畫效果
                    AnimationUtils.fadeIn(binding.rvStock, 400) {
                        AnimationUtils.fadeIn(binding.ivMenu, 300)
                    }
                    isFirstLoad = false
                } else {
                    // 後續更新的動畫效果
                    AnimationUtils.fadeIn(binding.rvStock, 200)
                }
            }

            // 在列表更新完成後恢復 scroll 位置
            if (savedScrollPosition > -1) {
                binding.rvStock.scrollToPosition(savedScrollPosition)
                savedScrollPosition = -1 // 重置位置
            }
        }
    }

    private fun saveCurrentScrollPosition() {
        val layoutManager = binding.rvStock.layoutManager as? LinearLayoutManager
        savedScrollPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
    }

    private fun showStockDetailDialog(message: String) {
        if (message.isEmpty()) return
        AlertDialog.Builder(this)
            .setTitle("股票詳細資訊")
            .setMessage(message)
            .setPositiveButton("確定") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 清理載入動畫
        binding.loadingView.stopAnimation()
    }

    override fun finish() {
        super.finish()
        // 設置退出動畫
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }
}