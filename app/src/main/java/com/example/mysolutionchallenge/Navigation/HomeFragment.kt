package com.example.mysolutionchallenge.Navigation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysolutionchallenge.Adapter.HomeAdapter
import com.example.mysolutionchallenge.HomeEditActivity
import com.example.mysolutionchallenge.Model.CategoryData
import com.example.mysolutionchallenge.Model.PillData
import com.example.mysolutionchallenge.Model.SharedViewModel
import com.example.mysolutionchallenge.R
import com.example.mysolutionchallenge.databinding.FragmentHomeBinding
import com.example.mytodolist.SharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var homeBinding: FragmentHomeBinding
    private var dataPosition = 0 //수정시 데이터를 가져오기위한 인덱스
    private var manager : LinearLayoutManager = LinearLayoutManager(activity)
    private var homeAdapter : HomeAdapter? = null
    private var data : MutableList<PillData?> = mutableListOf()
    //카테고리 이름 받아오는 데이터
    private var categoryName = ""
    //카테고리 이름을 전부 저장하는 배열
    private var categoryTempData = ArrayList<String>()
    private var sharedViewModel: SharedViewModel? = null

    val categoryArr : ArrayList<PillData> = ArrayList()
    private var selectCategoryData = HashMap<String, ArrayList<PillData>>()
    private var testselectCategoryData = HashMap<String, ArrayList<PillData>>()
    //상태유지
    var sharedPref : SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false)
        //tempData = data

        sharedPref = this.context?.let { SharedPref(it) }
        if (sharedPref!!.loadNightModeState()) {
            context?.setTheme(R.style.darktheme)
        } else {
            context?.setTheme(R.style.AppTheme)
        }


        initRecyclerView()
        initSwipeRefrech()

        homeBinding.homeItemAdd.setOnClickListener {
            val intent = Intent(activity, HomeEditActivity::class.java).apply {
                putExtra("type", "add")
                putExtra("categoryNameData", categoryTempData)
            }

            requestActivity.launch(intent)
            homeAdapter!!.notifyDataSetChanged()
        }

        //recyclerview item클릭 시
        homeAdapter!!.setItemClickListener(object :HomeAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, itemId: Int) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = data[position]
                    dataPosition = position
                    val intent = Intent(activity, HomeEditActivity::class.java).apply {
                        putExtra("type", "edit")
                        putExtra("item", todo)
                        putExtra("categoryNameData", categoryTempData)
                    }
                    requestActivity.launch(intent)
                }
            }
        })


        return homeBinding.root
    }

    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
        when (it.resultCode) {
            RESULT_OK -> {
                //getSerializableExtra = intent의 값을 보내고 받을때사용
                //타입 변경을 해주지 않으면 Serializable객체로 만들어지니 as로 캐스팅해주자
                val pill = it.data?.getSerializableExtra("pill") as PillData
                val selectCategory = it.data?.getSerializableExtra("cg") as String

                //선택한 카테고리 및 데이터 추가


                /*if (selectCategory.isNotEmpty()) {
                    selectCategoryData[selectCategory] = categoryArr
                }*/


                //api 33이후 아래로 변경됨
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSerializable(key, T::class.java)
                } else {
                    getSerializable(key) as? T
                }*/
                when(it.data?.getIntExtra("flag", -1)) {
                    //add
                    0 -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            data.add(pill)
                            categoryArr.add(pill)
                            //add면 그냥 추가
                            selectCategoryData[selectCategory] = categoryArr
                            //전
                            //println( categoryArr[dataPosition])
                        }
                        println("전 ${selectCategoryData[selectCategory]}")
                        //livedata
                        sharedViewModel!!.setCategoryLiveData("add", selectCategoryData)


                        homeAdapter!!.notifyDataSetChanged()
                        Toast.makeText(activity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    //edit
                    1 -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            data[dataPosition] = pill
                            categoryArr[dataPosition] = pill
                            selectCategoryData.clear()
                            selectCategoryData[selectCategory] = categoryArr
                            //후
                            //println(categoryArr[dataPosition])
                        }
                        println("선택 $selectCategory")
                        //livedata
                        sharedViewModel!!.categoryLiveData.value = selectCategoryData
                        println(testselectCategoryData)
                        homeAdapter!!.notifyDataSetChanged()
                        //Toast.makeText(activity, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                        Toast.makeText(activity, "$testselectCategoryData", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val testObserver = androidx.lifecycle.Observer<kotlin.collections.ArrayList<String>> { textValue ->
            categoryTempData = textValue
        }
        sharedViewModel!!.getLiveData().observe(viewLifecycleOwner, testObserver)


        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val categoryObserver = androidx.lifecycle.Observer<kotlin.collections.HashMap<String, ArrayList<PillData>>> { textValue ->
            testselectCategoryData = textValue
        }
        sharedViewModel!!.getCategoryLiveData().observe(viewLifecycleOwner, categoryObserver)
    }

    private fun initSwipeRefrech() {
        homeBinding.refreshSwipeLayout.setOnRefreshListener {
            //새로고침 시 터치불가능하도록
            requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) // 화면 터치 못하게 하기
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                //다시 가져온 데이터를 바탕으로 입력해줌
                homeAdapter!!.pillItemData = data
                homeBinding.recyclerView.startLayoutAnimation()
                homeBinding.refreshSwipeLayout.isRefreshing = false
                homeAdapter!!.notifyDataSetChanged()
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }, 1000)
            //터치불가능 해제ss
            //activity?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun initRecyclerView() {
        homeAdapter = HomeAdapter()
        homeAdapter!!.pillItemData = data
        homeBinding.recyclerView.adapter = homeAdapter
        //레이아웃 뒤집기 안씀
        //manager.reverseLayout = true
        //manager.stackFromEnd = true
        homeBinding.recyclerView.setHasFixedSize(true)
        homeBinding.recyclerView.layoutManager = manager
    }



    fun setCategoryData(name : String) {
        categoryName = name
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}