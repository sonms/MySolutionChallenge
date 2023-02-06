package com.example.mysolutionchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import com.example.mysolutionchallenge.Model.PillData
import com.example.mysolutionchallenge.databinding.ActivityHomeEditBinding
import java.util.*

class HomeEditActivity : AppCompatActivity() {
    private lateinit var homeEditBinding: ActivityHomeEditBinding
    private lateinit var timepicker : TimePicker
    private var id = 0
    private var setPill : PillData? = null
    private var pillTime : String? = ""
    private var eSetPill : PillData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeEditBinding = ActivityHomeEditBinding.inflate(layoutInflater)
        setContentView(homeEditBinding.root)

        val type = intent.getStringExtra("type")

        if (type.equals("add")) {
            homeEditBinding.pillEditBtn.text = "추가하기"
        } else if (type.equals("edit")) {
            eSetPill = intent.getSerializableExtra("item") as PillData?
            homeEditBinding.pillEdit.setText(eSetPill!!.pillName)
            homeEditBinding.pillEditBtn.text = "수정하기"
        }



        timepicker = homeEditBinding.pillTimepicker
        timepicker.setOnTimeChangedListener(TimePicker.OnTimeChangedListener { view, hourOfDay, minute ->
            pillTime = "$hourOfDay 시 $minute 분"
        })


        homeEditBinding.pillEditBtn.setOnClickListener {
            val pillContent = homeEditBinding.pillEdit.text.toString()
            if (type.equals("add")) {
                if (pillContent.isNotEmpty()) {
                    var pill = PillData(pillContent, pillTime!!)
                    val intent = Intent().apply {
                        putExtra("pill", pill)
                        putExtra("flag", 0)
                    }
                    id += 1
                    setResult(RESULT_OK, intent)
                    finish()
                }
            } else if (type.equals("edit")) {
                if (pillContent.isNotEmpty()) {
                    val ePill = PillData(pillContent, pillTime!!)

                    val intent = Intent().apply {
                        putExtra("pill", ePill)
                        putExtra("flag", 1)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}