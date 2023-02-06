package com.example.mysolutionchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mysolutionchallenge.Navigation.*
import com.example.mysolutionchallenge.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityMainBinding
    private lateinit var main_content : LinearLayout //xml의 content를 담는 layout
    private lateinit var bottom_navigationview : BottomNavigationView
    /*private var homeFragment : HomeFragment? = null
    private var blankFragment: BlankFragment? = null
    private var accountFragment: AccountFragment? = null*/
    private val TAG_HOME = "home_fragment"
    private val TAG_SEARCH = "search_fragment"
    private val TAG_ACCOUNT = "account_fragment"
    private val TAG_CATEGORY = "category_fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        super.onCreate(savedInstanceState)

        setContentView(view)
        setFragment(TAG_HOME, HomeFragment())
        initBottomNavigationBar()
    }

    private fun initBottomNavigationBar() {

        main_content = findViewById(R.id.main_content)
        bottom_navigationview = findViewById(R.id.bottomNavigationView)
        //바텀네비게이션 음영 삭제
        bottom_navigationview.background = null
        //가운데 메뉴가 비워져 있기 때문에 비활성화화
        bottom_navigationview.menu.getItem(2).isEnabled = false

        mBinding.chatFab.setOnClickListener {
            Toast.makeText(this, "chat", Toast.LENGTH_SHORT).show()
        }

        mBinding.bottomNavigationView.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.home -> {

                    /*var manager = findViewById<RecyclerView>(R.id.recyclerView).layoutManager as? LinearLayoutManager
                    //smooth하게 올리는 방식 빠르게올리는 방식은 아래 scrollToLastItem
                    val smoothScroller = object : LinearSmoothScroller(this) {
                        override fun getVerticalSnapPreference(): Int {
                            return LinearSmoothScroller.SNAP_TO_END
                        }
                    }
                    val last = manager!!.findFirstCompletelyVisibleItemPosition()
                    smoothScroller.targetPosition = 0
                    if (last >= 0) {
                        //println(last)
                        manager!!.startSmoothScroll(smoothScroller)
                    } else {
                        setFragment(TAG_HOME, HomeFragment())
                    }*/
                    setFragment(TAG_HOME, HomeFragment())
                }
                R.id.category -> setFragment(TAG_CATEGORY, CategoryFragment())
                R.id.account -> setFragment(TAG_ACCOUNT, AccountFragment())
                R.id.search -> setFragment(TAG_SEARCH, SearchFragment())
            }
            true
        }
    }

    fun setFragment(tag : String, fragment: Fragment) {
        val manager : FragmentManager = supportFragmentManager
        val bt = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            bt.add(R.id.main_content, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val category = manager.findFragmentByTag(TAG_CATEGORY)
        val account = manager.findFragmentByTag(TAG_ACCOUNT)
        val search = manager.findFragmentByTag(TAG_SEARCH)

        if (home != null) {
            bt.hide(home)
        }
        if (category != null) {
            bt.hide(category)
        }
        if (account != null) {
            bt.hide(account)
        }
        if (search != null) {
            bt.hide(search)
        }
        /////////////////
        if (tag == TAG_HOME) {
            if (home != null) {
                bt.show(home)
            }
        }
        else if (tag == TAG_SEARCH) {
            if (search != null) {
                bt.show(search)
            }
        }
        else if (tag == TAG_ACCOUNT) {
            if (account != null) {
                bt.show(account)
            }
        }
        else if (tag == TAG_CATEGORY) {
            if (category != null) {
                bt.show(category)
            }
        }

        bt.commitAllowingStateLoss()
    }

}