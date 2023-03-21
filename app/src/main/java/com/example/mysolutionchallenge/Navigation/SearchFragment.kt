package com.example.mysolutionchallenge.Navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.compose.runtime.mutableStateListOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysolutionchallenge.Adapter.HomeAdapter
import com.example.mysolutionchallenge.Adapter.SearchAdapter
import com.example.mysolutionchallenge.Adapter.SearchItemAdapter
import com.example.mysolutionchallenge.HomeEditActivity
import com.example.mysolutionchallenge.MainActivity
import com.example.mysolutionchallenge.Model.MedicalData
import com.example.mysolutionchallenge.Model.PillData
import com.example.mysolutionchallenge.Model.SearchWordData
import com.example.mysolutionchallenge.SearchItemViewActivity
import com.example.mysolutionchallenge.databinding.FragmentSearchBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Context를 액티비티로 형변환해서 할당
        mainActivity = context as MainActivity
    }

    private lateinit var mBinding : FragmentSearchBinding
    private var manager : LinearLayoutManager = LinearLayoutManager(activity)
    private var isEnter = true
    //검색어
    private var searchAdapter : SearchAdapter? = null
    //검색 후 내용
    private var searchItemAdapter : SearchItemAdapter? = SearchItemAdapter()
    private var filterData = mutableListOf<MedicalData?>()
    //파이어베이스 데이터 연결
    private lateinit var medicalRTD : DatabaseReference
    private lateinit var pillDataRTD : DatabaseReference


    //검색 기록용
    //검색 개수(id)
    var searchId = 0
    //검색용
    private var searchWordList = mutableListOf<SearchWordData?>()
    //검색 후 나올 내용을 담음
    var searchItemPosition = 0
    private var allData = mutableStateListOf<MedicalData>()
    private var searchItemList = mutableListOf<SearchWordData>()
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
    ): View {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false)
        //firebase 연동
        //firebase app 초기화 연결
        FirebaseApp.initializeApp(mainActivity)

        //database 연결
        val database = Firebase.database
        medicalRTD = database.getReference("medical")
        pillDataRTD = database.getReference("PillData")

        getSearchData()

        /*private lateinit var realTimeDatabase = Firebase.database
        val myRTD = realTimeDatabase.getReference("medical")*/

        initWordRecyclerView()
        initItemRecyclerView()

        //자동으로 키보드 띄우기
        searchViewkeyBoard(isEnter)
        //뒤로가기 버튼 생성 -> 의미없음 메뉴에있는거라
        //(activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        mBinding.searchView.queryHint = "검색할 증상 및 병명을 입력해 주세요"
        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //검색버튼 눌렀을 때 실행
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    //검색 기록
                    searchWordList.add(SearchWordData(searchId, query))
                    print(query)
                    print(searchWordList)
                    searchId += 1
                    isEnter = false
                    searchAdapter!!.notifyDataSetChanged()

                    mBinding.searchWordRv.visibility = View.GONE
                    mBinding.searchItemRv.visibility = View.VISIBLE



                    //검색 기능
                    val filterString = query.toString().lowercase(Locale.getDefault()).trim {it < ' '}

                    for (searchItem in allData) {
                        if (searchItem!!.content!!.lowercase(Locale.getDefault()).contains(filterString)) {
                            //println("tem - $tem")
                            filterData.add(searchItem)
                            searchItemAdapter!!.notifyDataSetChanged()
                            println(filterData)
                        }
                    }



                    /*searchWordList.add(SearchWordData(searchId, query))
                    //앱을 종료 후에도 기록이 남도록
                    sharedPref!!.setSearchHistory(this@SearchActivity, setKey, searchWordList)
                    searchWordAdapter!!.notifyDataSetChanged()
                    searchId += 1
                    isEnter = false
                    println(query)
                    //println(searchWordList)
                    /*CoroutineScope(Dispatchers.IO).launch {
                        val intent = Intent(this@SearchActivity, SearchViewAcitivity::class.java).apply {
                            putExtra("searchword", query)
                        }
                        startActivity(intent)
                    }*/
                    val intent = Intent().apply {
                        putExtra("SEARCH", query)
                        putExtra("flag",4)
                    }
                    setResult(RESULT_SEARCH, intent)
                    finish()*/
                } else {

                }
                return true
            }
            //검색창에 값이 입력될 때 마다 실행
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    mBinding.searchWordRv.visibility = View.VISIBLE
                    mBinding.searchItemRv.visibility = View.GONE
                }
                return true
            }
        })



        /*homeAdapter!!.setItemClickListener(object : HomeAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, itemId: Int) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = data[position]
                    dataPosition = position
                    val intent = Intent(activity, HomeEditActivity::class.java).apply {
                        putExtra("type", "edit")
                        putExtra("item", todo)
                    }
                    requestActivity.launch(intent)
                }
            }
        })*/
        searchAdapter!!.setItemClickListener(object : SearchAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, itemId: Int) {
                val searchWord = searchWordList[position]
                Toast.makeText(activity, "$searchWord", Toast.LENGTH_SHORT).show()
            }
        })

        searchItemAdapter!!.setItemClickListener(object : SearchItemAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, itemId: Int) {
                CoroutineScope(Dispatchers.IO).launch {
                    val searchitem = filterData[position]
                    //dataPosition = position
                    val intent = Intent(activity, SearchItemViewActivity::class.java).apply {
                        putExtra("type", "view")
                        putExtra("item", searchitem)
                    }
                    requestActivity.launch(intent)
                }
            }
        })



        return mBinding.root
    }

    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
        when (it.resultCode) {
            RESULT_OK -> {
                val pill = it.data?.getSerializableExtra("pill") as PillData

                when (it.data?.getIntExtra("flag", -1)) {
                    0 -> {
                        Toast.makeText(activity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initWordRecyclerView() {
        searchAdapter = SearchAdapter()
        searchAdapter!!.searchWordData = searchWordList
        mBinding.searchWordRv.adapter = searchAdapter
        mBinding.searchWordRv.layoutManager = manager
        mBinding.searchWordRv.setHasFixedSize(true)
    }

    private fun initItemRecyclerView() {
        searchItemAdapter = SearchItemAdapter()
        searchItemAdapter!!.searchItemData = filterData
        mBinding.searchItemRv.adapter = searchItemAdapter
        mBinding.searchItemRv.layoutManager = LinearLayoutManager(activity)
        mBinding.searchItemRv.setHasFixedSize(true)
    }



    private fun searchViewkeyBoard(isEnter : Boolean) {
        mBinding.searchView.isIconified = !isEnter
    }

    private fun getSearchData() {
        /*val postListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                allData.clear()
                m_nCount = dataSnapshot.childrenCount.toInt()
                var name: String
                for (postSnapshot in dataSnapshot.children) {
                    if (postSnapshot.child("username").getValue(String::class.java) != null) {
                        name = postSnapshot.child("username").getValue(String::class.java)
                        m_arritems.add(name)
                    }
                }
                m_adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("FirebaseDatabase", "onCancelled", databaseError.toException())
            }
        }*/
        medicalRTD.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ss in snapshot.children) {
                    val temp = ss.value as Map<*, *>
                    println(temp.filter { it.key == "sub_title"})
                    allData.add(MedicalData(
                        temp.filter { it.key == "sub_title"}.toString(),
                        temp.filter { it.key == "title"}.toString(),
                        temp.filter { it.key == "content"}.toString(),
                        searchItemPosition
                        ))
                    searchItemPosition += 1
                    //println(temp.filter { it.key == "title" })
                    //println(temp.filter { it.key == "content" })
                    //allData.add(MedicalData(temp[1].toString()))
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val ref = FirebaseDatabase.getInstance().getReference("/PillData")
        ref.orderByChild("id").equalTo("200808877").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val result = snapshot.getValue<HashMap<String, String>>()
                println(result)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) { }

        })


    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}