package com.example.mysolutionchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.mysolutionchallenge.Model.MedicalData
import com.example.mysolutionchallenge.Model.PillData
import com.example.mysolutionchallenge.databinding.ActivitySearchitemviewBinding
import com.example.mytodolist.SharedPref

class SearchItemViewActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivitySearchitemviewBinding
    private var setMedicalContent : MedicalData? = null

    //상태유지
    var sharedPref : SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = SharedPref(this)
        ///테마 변경///
        if (sharedPref!!.loadNightModeState()) {
            setTheme(R.style.darktheme)
        } else {
            setTheme(R.style.AppTheme)
        }
        if (sharedPref!!.loadSmallModeState()) {
            setTheme(R.style.smalltextsizetheme)
        } else {
            setTheme(R.style.AppTheme)
        }
        if (sharedPref!!.loadMiddleModeState()) {
            setTheme(R.style.mediumtextsizetheme)
        } else {
            setTheme(R.style.AppTheme)
        }
        if (sharedPref!!.loadLargeModeState()) {
            setTheme(R.style.largetextsizetheme)
        } else {
            setTheme(R.style.AppTheme)
        }
        /////////////////////////////////
        mBinding = ActivitySearchitemviewBinding.inflate(layoutInflater)


        setContentView(mBinding.root)

        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val type = intent.getStringExtra("type")

        if (type.equals("view")) {
            setMedicalContent = intent.getSerializableExtra("item") as MedicalData?
            mBinding.medicalTitle.text = setMedicalContent!!.title.toString()
            mBinding.medicalContent.text = setMedicalContent!!.content.toString()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.home -> {
                val intent = Intent().apply {

                }
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}