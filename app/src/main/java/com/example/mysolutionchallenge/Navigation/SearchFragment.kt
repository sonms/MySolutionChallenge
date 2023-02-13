package com.example.mysolutionchallenge.Navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysolutionchallenge.Adapter.SearchAdapter
import com.example.mysolutionchallenge.Model.SearchWordData
import com.example.mysolutionchallenge.databinding.FragmentSearchBinding

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


    private lateinit var mBinding : FragmentSearchBinding
    private var manager : LinearLayoutManager = LinearLayoutManager(activity)
    private var isEnter = true

    private var searchAdapter : SearchAdapter? = null


    //검색 기록용
    //검색 개수(id)
    var searchId = 0
    //검색용
    private var searchWordList = mutableListOf<SearchWordData?>()
    //검색 후 나올 내용을 담음
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

        initWordRecyclerView()

        //자동으로 키보드 띄우기
        searchViewkeyBoard(isEnter)
        //뒤로가기 버튼 생성
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        mBinding.searchView.queryHint = "검색할 증상 및 병명을 입력해 주세요"
        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //검색버튼 눌렀을 때 실행
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchWordList.add(SearchWordData(searchId, query))
                    print(query)
                    print(searchWordList)
                    searchId += 1
                    isEnter = false
                    searchAdapter!!.notifyDataSetChanged()
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
                print(newText)
                return true
            }
        })



        return mBinding.root
    }

    private fun initWordRecyclerView() {
        searchAdapter = SearchAdapter()
        searchAdapter!!.searchWordData = searchWordList
        mBinding.searchWordRv.adapter = searchAdapter
        mBinding.searchWordRv.layoutManager = manager
        mBinding.searchWordRv.setHasFixedSize(true)
    }

    private fun searchViewkeyBoard(isEnter : Boolean) {
        mBinding.searchView.isIconified = !isEnter
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