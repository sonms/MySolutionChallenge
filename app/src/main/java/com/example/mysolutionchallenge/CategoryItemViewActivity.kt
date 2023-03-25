package com.example.mysolutionchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysolutionchallenge.Adapter.CategoryItemAdapter
import com.example.mysolutionchallenge.Adapter.HomeAdapter
import com.example.mysolutionchallenge.Model.PillData
import com.example.mysolutionchallenge.Model.SharedViewModel
import com.example.mysolutionchallenge.databinding.ActivityCameraBinding
import com.example.mysolutionchallenge.databinding.ActivityCategoryItemViewBinding

class CategoryItemViewActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityCategoryItemViewBinding
    private var manager : LinearLayoutManager = LinearLayoutManager(this)
    private var categoryItemAdapter : CategoryItemAdapter? = null
    private var categoryItemPillData = mutableListOf<PillData?>()
    var t : ArrayList<List<PillData>> = ArrayList()
    //viewmodel
    private var sharedViewModel: SharedViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCategoryItemViewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initRecyclerView()

        val type = intent.getStringExtra("type")

        if (type.equals("categoryItemView")) {
            t = intent.getSerializableExtra("item") as ArrayList<List<PillData>>
        }

        mBinding.ctestbtn.setOnClickListener {
            t.forEach {
                println(it)
            }

        }
    }

   /*private fun createViewModel() {
       sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
       val categoryViewObserver = androidx.lifecycle.Observer<kotlin.collections.HashMap<String, PillData>> { textValue ->
           selectCategoryData = textValue
       }
       sharedViewModel!!.getCategoryLiveData().observe(this, categoryViewObserver)
   }*/

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