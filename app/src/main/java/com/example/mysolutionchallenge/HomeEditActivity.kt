package com.example.mysolutionchallenge

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import com.example.mysolutionchallenge.Helper.AlertReceiver
import com.example.mysolutionchallenge.Model.CategoryData
import com.example.mysolutionchallenge.Model.PillData
import com.example.mysolutionchallenge.databinding.ActivityHomeEditBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeEditActivity : AppCompatActivity() {
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
    private var setCategoryData : ArrayList<CategoryData?> = ArrayList()
    private var s = ""
    private var passedIntent = intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeEditBinding = ActivityHomeEditBinding.inflate(layoutInflater)
        setContentView(homeEditBinding.root)

        val type = intent.getStringExtra("type")
        val cType = intent.getStringExtra("cType")

        if (type.equals("add")) {
            homeEditBinding.pillEditBtn.text = "추가하기"
        } else if (type.equals("edit")) {
            eSetPill = intent.getSerializableExtra("item") as PillData?
            homeEditBinding.pillEdit.setText(eSetPill!!.pillName)
            homeEditBinding.pillEditBtn.text = "수정하기"
        }

        if (cType.equals("category")) {
            setCategoryData = intent.getSerializableExtra("categoryData") as ArrayList<CategoryData?>
        } else {
            println("%%%%%%%%%%%%%%%5null")
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
                println("$setCategoryData")
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