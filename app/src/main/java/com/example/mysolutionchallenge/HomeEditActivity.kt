package com.example.mysolutionchallenge

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.mysolutionchallenge.Helper.AlertReceiver
import com.example.mysolutionchallenge.Model.CategoryData
import com.example.mysolutionchallenge.Model.PillData
import com.example.mysolutionchallenge.databinding.ActivityHomeEditBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeEditActivity : AppCompatActivity() {
    //뒤로가기
    private val TAG = this.javaClass.simpleName
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
    ///
    private lateinit var homeEditBinding: ActivityHomeEditBinding
    private lateinit var timepicker : TimePicker
    //데이터 추가 및 수정
    private var id = 0
    private var setPill : PillData? = null
    private var pillTime : String? = ""
    private var eSetPill : PillData? = null
    //알람설정
    private var setAlarmTime : Calendar? = null
    //카테고리 데이터
    private var setCategoryData : ArrayList<String> = ArrayList() //스피너역할까지
    private var pos = 0
    private var categoryString = ""
    //스피너
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeEditBinding = ActivityHomeEditBinding.inflate(layoutInflater)
        setContentView(homeEditBinding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.onBackPressedDispatcher.addCallback(this, callback)

        val type = intent.getStringExtra("type")

        if (type.equals("add")) {
            homeEditBinding.pillEditBtn.text = "추가하기"
            setCategoryData = intent.getSerializableExtra("categoryNameData") as ArrayList<String>

            if (setCategoryData.isNotEmpty()) {
                val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, setCategoryData)
                homeEditBinding.spinner.adapter = myAdapter
                pos = setCategoryData.size
            }

        } else if (type.equals("edit")) {
            eSetPill = intent.getSerializableExtra("item") as PillData?
            homeEditBinding.pillEdit.setText(eSetPill!!.pillName)
            homeEditBinding.pillEditBtn.text = "수정하기"
        }

        homeEditBinding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                homeEditBinding.spinnerTV.text = setCategoryData[position]
                categoryString = setCategoryData[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@HomeEditActivity, "카테고리를 선택하지 않으셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }



        timepicker = homeEditBinding.pillTimepicker
        timepicker.setOnTimeChangedListener(TimePicker.OnTimeChangedListener { view, hourOfDay, minute ->

            setAlarmTime = Calendar.getInstance()
            pillTime = "$hourOfDay 시 $minute 분"

            setAlarmTime!!.set(Calendar.HOUR_OF_DAY, hourOfDay)
            setAlarmTime!!.set(Calendar.MINUTE, minute)
            setAlarmTime!!.set(Calendar.SECOND, 0)
        })


        homeEditBinding.pillEditBtn.setOnClickListener {
            val pillContent = homeEditBinding.pillEdit.text.toString()
            if (type.equals("add")) {
                if (pillContent.isNotEmpty()) {
                    if (setAlarmTime == null) {
                        setAlarmTime = Calendar.getInstance()
                        val h = setAlarmTime!!.get(Calendar.HOUR_OF_DAY)
                        val m = setAlarmTime!!.get(Calendar.MINUTE)
                        pillTime = "$h 시 $m 분"
                    }
                    var pill = PillData(id, pillContent, pillTime!!)

                    val intent = Intent().apply {
                        putExtra("pill", pill)
                        putExtra("cg", categoryString)
                        putExtra("flag", 0)
                    }
                    id += 1
                    //알람 설정
                    if (setAlarmTime != null) {
                        startAlarm(setAlarmTime!!, pillContent)
                    }

                    setResult(RESULT_OK, intent)
                    finish()
                }
            } else if (type.equals("edit")) {
                if (pillContent.isNotEmpty()) {
                    val ePill = PillData(eSetPill!!.position , pillContent, pillTime!!)

                    val intent = Intent().apply {
                        putExtra("pill", ePill)
                        putExtra("flag", 1)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }

        homeEditBinding.testbtn.setOnClickListener {
            if (setCategoryData.size == 0) {
                println("null")
            } else {
                setCategoryData.forEach {
                    println(it)
                }
            }
        }
    }

    /*private fun processIntent(intent : Intent) {
        if (intent != null) {
            setCategoryData = intent.getSerializableExtra("categoryData") as ArrayList<CategoryData?>
            if (setCategoryData != null) {
                Toast.makeText(this, setCategoryData.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                val intent = Intent().apply {

                }
                setResult(7, intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //알림 설정
    private fun startAlarm(c : Calendar, content : String?) {
        var alarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var curTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.time)

        var bundle = Bundle()
        bundle.putString("time", curTime)
        bundle.putString("content", content)

        var intent = Intent(this, AlertReceiver::class.java).apply {
            putExtra("bundle",bundle)
        }

        //intent를 당장 수행하지 않고 특정시점에 수행하도록 미룰 수 있는 intent
        var pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0).apply {

        }

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, setAlarmTime!!.timeInMillis, pendingIntent)

    }

    //알림 취소
    private fun cancelAlarm() {
        var alarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var intent = Intent(this, AlertReceiver::class.java)

        //intent를 당장 수행하지 않고 특정시점에 수행하도록 미룰 수 있는 intent
        var pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)

        alarmManager.cancel(pendingIntent)
    }
}