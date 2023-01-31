package com.example.mysolutionchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import com.example.mysolutionchallenge.databinding.ActivityHomeEditBinding
import java.util.*

class HomeEditActivity : AppCompatActivity() {
    private lateinit var homeEditBinding: ActivityHomeEditBinding
    private var pillTime : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_edit)

        val timePicker = findViewById<TimePicker>(R.id.pill_timepicker)
        timePicker.setOnTimeChangedListener(TimePicker.OnTimeChangedListener { view, hourOfDay, minute ->
            pillTime = "$hourOfDay 시 $minute 분으로 설정"
        })
    }
}