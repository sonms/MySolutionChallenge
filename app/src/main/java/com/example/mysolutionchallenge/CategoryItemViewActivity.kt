package com.example.mysolutionchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysolutionchallenge.Adapter.CategoryItemAdapter
import com.example.mysolutionchallenge.Adapter.HomeAdapter
import com.example.mysolutionchallenge.Model.PillData
import com.example.mysolutionchallenge.databinding.ActivityCameraBinding
import com.example.mysolutionchallenge.databinding.ActivityCategoryItemViewBinding

class CategoryItemViewActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityCategoryItemViewBinding
    private var manager : LinearLayoutManager = LinearLayoutManager(this)
    private var categoryItemAdapter : CategoryItemAdapter? = null
    private var categoryItemPillData = mutableListOf<PillData?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCategoryItemViewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initRecyclerView()

        val type = intent.getStringExtra("type")
    }

    private fun initRecyclerView() {
        categoryItemAdapter = CategoryItemAdapter()
        categoryItemAdapter!!.categoryItemData = categoryItemPillData
        mBinding.categoryItemRV.adapter = categoryItemAdapter
        //레이아웃 뒤집기 안씀
        //manager.reverseLayout = true
        //manager.stackFromEnd = true
        mBinding.categoryItemRV.setHasFixedSize(true)
        mBinding.categoryItemRV.layoutManager = manager
    }
}