package com.example.mysolutionchallenge

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysolutionchallenge.Adapter.CategoryItemAdapter
import com.example.mysolutionchallenge.Adapter.HomeAdapter
import com.example.mysolutionchallenge.Model.CategoryData
import com.example.mysolutionchallenge.Model.PillData
import com.example.mysolutionchallenge.Model.SharedViewModel
import com.example.mysolutionchallenge.databinding.ActivityCameraBinding
import com.example.mysolutionchallenge.databinding.ActivityCategoryItemViewBinding
import com.example.mytodolist.SharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryItemViewActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityCategoryItemViewBinding
    private var manager : LinearLayoutManager = LinearLayoutManager(this)
    private var categoryItemAdapter : CategoryItemAdapter? = null
    private var categoryItemPillData : ArrayList<PillData> = ArrayList()
    //var t : ArrayList<List<PillData>> = ArrayList()
    private var sharedPref : SharedPref? = null
    //var st : List<PillData> = ArrayList()
    private var dataPosition = 0
    var categoryDataSent : ArrayList<PillData>? = ArrayList()
    //viewmodel
    private var sharedViewModel: SharedViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCategoryItemViewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        sharedPref = SharedPref(this)
        if (sharedPref!!.loadNightModeState()) {
            setTheme(R.style.darktheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        initRecyclerView()

        val type = intent.getStringExtra("type")

        if (type.equals("categoryItemView")) {
            //t = intent.getSerializableExtra("item") as ArrayList<List<PillData>>
            categoryDataSent = intent.getSerializableExtra("item") as ArrayList<PillData>?
            //st = t[0] //as ArrayList<PillData>
            for (i in categoryDataSent!!.indices) {
                categoryItemPillData.add(categoryDataSent?.get(i)!!)
            }

        }

        //recyclerview item클릭 시
        categoryItemAdapter!!.setItemClickListener(object :CategoryItemAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, itemId: Int) {
                CoroutineScope(Dispatchers.IO).launch {
                    val categoryItem = categoryItemPillData[position]
                    dataPosition = position
                    val intent = Intent(this@CategoryItemViewActivity, HomeEditActivity::class.java).apply {
                        putExtra("type", "edit")
                        putExtra("item", categoryItem)
                    }
                    requestActivity.launch(intent)
                }
            }
        })

        mBinding.ctestbtn.setOnClickListener {
            //println(te)
            println(categoryItemPillData)
        }
    }
    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
        when (it.resultCode) {
            RESULT_OK -> {
                //getSerializableExtra = intent의 값을 보내고 받을때사용
                //타입 변경을 해주지 않으면 Serializable객체로 만들어지니 as로 캐스팅해주자
                val categoryItem = it.data?.getSerializableExtra("pill") as PillData
                when(it.data?.getIntExtra("flag", -1)) {
                    1 -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            categoryItemPillData[dataPosition] = categoryItem
                        }
                        categoryItemAdapter!!.notifyDataSetChanged()
                        Toast.makeText(this, "${categoryItemPillData[dataPosition]}", Toast.LENGTH_SHORT).show()
                        println(categoryItem)
                    }
                }
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