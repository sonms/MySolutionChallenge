package com.example.mysolutionchallenge

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mysolutionchallenge.Navigation.*
import com.example.mysolutionchallenge.databinding.ActivityMainBinding
import com.example.mytodolist.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), FragmentListener {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var main_content: LinearLayout //xml의 content를 담는 layout
    private lateinit var bottom_navigationview: BottomNavigationView
    private var categoryFragment : CategoryFragment? = null
    private var homeFragment : HomeFragment? = null

    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it
    }

    /*private var homeFragment : HomeFragment? = null
    private var blankFragment: BlankFragment? = null
    private var accountFragment: AccountFragment? = null*/
    private val TAG_HOME = "home_fragment"
    private val TAG_SEARCH = "search_fragment"
    private val TAG_ACCOUNT = "account_fragment"
    private val TAG_CATEGORY = "category_fragment"
    private val TAG_CAMERA = "camera_fragment"
    private var sharedPref : SharedPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = SharedPref(this)
        if (sharedPref!!.loadNightModeState()) {
            setTheme(R.style.darktheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        super.onCreate(savedInstanceState)

        categoryFragment = supportFragmentManager.findFragmentById(R.id.category) as CategoryFragment?
        homeFragment = supportFragmentManager.findFragmentById(R.id.home) as HomeFragment?

        setContentView(view)
        setFragment(TAG_HOME, HomeFragment())
        initBottomNavigationBar()
        requestPermission()
    }

    private fun initBottomNavigationBar() {

        main_content = findViewById(R.id.main_content)
        bottom_navigationview = findViewById(R.id.bottomNavigationView)
        //바텀네비게이션 음영 삭제
        bottom_navigationview.background = null
        //가운데 메뉴가 비워져 있기 때문에 비활성화화
        //bottom_navigationview.menu.getItem(2).isEnabled = false

        /*mBinding.chatFab.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity.applicationContext,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
            }

            if (ContextCompat.checkSelfPermission(
                    this@MainActivity.applicationContext,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }*/

        mBinding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
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
                R.id.cameraMenu -> {
                    setFragment(TAG_CAMERA, CameraFragment())
                }
                R.id.category -> setFragment(TAG_CATEGORY, CategoryFragment())
                R.id.account -> setFragment(TAG_ACCOUNT, AccountFragment())
                R.id.search -> setFragment(TAG_SEARCH, SearchFragment())
            }
            true
        }
    }

    fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val bt = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            bt.add(R.id.main_content, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val category = manager.findFragmentByTag(TAG_CATEGORY)
        val account = manager.findFragmentByTag(TAG_ACCOUNT)
        val search = manager.findFragmentByTag(TAG_SEARCH)
        val camera = manager.findFragmentByTag(TAG_CAMERA)

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
        if (camera != null) {
            bt.hide(camera)
        }
        /////////////////
        if (tag == TAG_HOME) {
            if (home != null) {
                bt.show(home)
            }
        } else if (tag == TAG_SEARCH) {
            if (search != null) {
                bt.show(search)
            }
        } else if (tag == TAG_ACCOUNT) {
            if (account != null) {
                bt.show(account)
            }
        } else if (tag == TAG_CATEGORY) {
            if (category != null) {
                bt.show(category)
            }
        } else if (tag == TAG_CAMERA) {
            if (camera != null) {
                bt.show(camera)
            }
        }

        bt.commitAllowingStateLoss()
    }

    fun requestPermission() {
        val per1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val per2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if (per1 == PackageManager.PERMISSION_DENIED || per2 == PackageManager.PERMISSION_DENIED) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                1000
            )
        }
    }

    override fun onCommand(index: Int, message: String?) {
        if (index == 0) {
            homeFragment!!.setCategoryData(message!!)
        }
    }
}